package com.booking.system.showbookingsystem.platform;

import com.booking.system.showbookingsystem.entity.Booking;
import com.booking.system.showbookingsystem.entity.Show;
import com.booking.system.showbookingsystem.entity.Ticket;
import com.booking.system.showbookingsystem.service.BookingService;
import com.booking.system.showbookingsystem.service.ShowService;
import com.booking.system.showbookingsystem.service.TicketService;
import com.booking.system.showbookingsystem.utils.BookingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Component
public class MenuInputter implements CommandLineRunner {
    ShowService showService;
    BookingService bookingService;
    TicketService ticketService;
    final Clock clock;
    @Value("${cancellation.window}")
    private int cancellationInMinutes;

    @Autowired
    public MenuInputter(TicketService ticketService, BookingService bookingService, ShowService showService, Clock clock) {
        this.ticketService = ticketService;
        this.bookingService = bookingService;
        this.showService = showService;
        this.clock = clock;
    }

    public void displayOptions() {
        String choice;
        Scanner scanner = new Scanner(System.in);
        boolean logout = false;
        do {

            System.out.println("Welcome to the booking system!");
            System.out.println("Please specify which role number you would like to log in as: ");
            System.out.println("[1] Admin");
            System.out.println("[2] Buyer");
            System.out.println("If you would like to log out, please type exit");

            choice = scanner.nextLine().toLowerCase();
            final String ADMIN = "1";
            final String BUYER = "2";
            final String EXIT = "exit";
            switch (choice) {
                case ADMIN -> adminOptions();
                case BUYER -> buyerOptions();
                case EXIT -> logout = true;
                default -> System.out.println("Invalid choice, please try again!");
            }
        } while (!logout);
        BookingUtils.showExitMessage();
        scanner.close();
    }

    public void adminOptions() {

        Scanner scanner = new Scanner(System.in);

        boolean logout = false;
        do {
            System.out.println("These are the actions you can perform:");
            System.out.println("[1] Setup a new show");
            System.out.println("[2] View an existing show");
            System.out.println("[3] Log out as an admin");
            String choice = scanner.nextLine();
            switch (choice.toLowerCase()) {
                case "1" -> createShow();
                case "2" -> viewShowAdmin();
                case "3" -> {
                    System.out.println("Logging out now...");
                    logout = true;
                }
                default -> System.out.println("Invalid choice, please try again!");
            }
        } while (!logout);
    }

    public void buyerOptions() {
        Scanner scanner = new Scanner(System.in);

        boolean logout = false;
        do {
            System.out.println("These are the actions you can perform:");
            System.out.println("[1] Check the availability of a show");
            System.out.println("[2] Book a show");
            System.out.println("[3] Cancel a booking");
            System.out.println("[4] Log out as a buyer");
            String choice = scanner.nextLine();

            switch (choice.toLowerCase()) {
                case "1" -> viewShowsBuyer();
                case "2" -> bookShow();
                case "3" -> cancelTickets();
                case "4" -> {
                    System.out.println("Logging out now...");
                    logout = true;
                }
                default -> System.out.println("Invalid choice, please try again!");
            }

        } while (!logout);
    }


    private void bookShow() {
        Scanner scanner = new Scanner(System.in);
        Long showNum;
        Show show = null;
        //TODO disable shows with no seats available from being booked
        do {
            System.out.println("Please enter the show number you would like to book: ");
            showNum = BookingUtils.parseLong(scanner.next());
            if (showNum != null) {
                show = showService.getShowById(showNum);
                if (show == null) {
                    System.out.println("Show number does not exist, please try again!");
                }
            }
        } while (show == null);

        String phoneNum;

        do {
            System.out.println("Please enter the phone number you would like to book with: ");
            phoneNum = scanner.next();
            boolean isValid = BookingUtils.checkIfValidPhoneNumber(phoneNum);
            if (!isValid) {
                System.out.println("Phone number is invalid, please try again!");
                phoneNum = null;
            }
            if (bookingService.getBookingByShowNumberAndBuyerPhone(show.getShowNumber(), phoneNum) != null) {
                System.out.println("A booking with this phone number exists for this show, please try again!");
                phoneNum = null;
            }
        } while (phoneNum == null);

        Map<String, List<String>> seatsMap = returnAvailableSeats(show);
        List<String> seatsToBook;

        do {
            System.out.println("Please enter the seats you would like to book [separated by comma]: ");
            seatsToBook = BookingUtils.parseCommaSeparatedString(scanner.next());
            if (seatsToBook != null) {
                if (!BookingUtils.checkIfSeatsToBookAreValid(seatsMap, seatsToBook)) {
                    System.out.println("Invalid seats selected, please try again!");
                    seatsToBook = null;
                }
            }
        } while (seatsToBook == null);
        String bookingId = BookingUtils.generateBookingId();
        List<Ticket> ticketList = BookingUtils.generateTicketsFromSeats(bookingId, seatsToBook);
        LocalDateTime cancellationDateTime = BookingUtils.getCancellationDateTime(clock, show.getCancellationTime());
        bookingService.create(new Booking(bookingId, show.getShowNumber(), phoneNum, ticketList, cancellationDateTime));
        System.out.println("Your booking has been completed and can only be cancelled " +
                "before " + cancellationDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)) + "!");
        System.out.println("Please take note of the following ticket numbers:");
        ticketList.forEach(ticket -> System.out.println("Ticket #: " + ticket.getTicketId()));
        System.out.println();
    }

    private void cancelTickets() {
        Scanner scanner = new Scanner(System.in);
        String phoneNum;
        List<Booking> bookings = null;
        do {
            System.out.println("Please enter the phone number of the tickets you booked: ");
            phoneNum = scanner.next();
            boolean isValid = BookingUtils.checkIfValidPhoneNumber(phoneNum);
            if (!isValid) {
                System.out.println("Phone number is invalid, please try again!");
                phoneNum = null;
            }
            if (phoneNum != null) {
                bookings = bookingService.getBookingsByBuyerPhone(phoneNum);
                if (bookings == null) {
                    System.out.println("No bookings can be found for this phone number, please try again!");
                }
            }
        } while (phoneNum == null || bookings == null);

        printBookingsAndTicketsBuyer(bookings);
        String ticketId;
        do {
            System.out.println("Please enter the ticket # you wish to cancel: ");
            ticketId = scanner.next();
            boolean isValid = BookingUtils.checkIfTicketNumberExistsWithinBookings(ticketId, bookings);
            if (!isValid) {
                System.out.println("Ticket Number does not exist within the bookings listed, please try again!");
                ticketId = null;
            }
        } while (ticketId == null);

        Ticket ticketToCancel = ticketService.getTicketById(ticketId);
        boolean bookingDeleted = bookingService.deleteTicketFromBooking(ticketToCancel.getBookingId(), ticketToCancel);

        System.out.println("The ticket has been cancelled.");
        if (bookingDeleted) {
            System.out.println("As there are no other tickets in the booking, the booking has been cancelled.");
        }
        System.out.println();
    }

    private void createShow() {
        Scanner scanner = new Scanner(System.in);
        Long showNum;
        do {
            System.out.println("Please enter the show number");
            showNum = BookingUtils.parseLong(scanner.next());
            if (showService.getShowById(showNum) != null) {
                System.out.println("Show number should be unique, please try again!");
                showNum = null;
            }
        } while (showNum == null);

        int rows;
        do {
            System.out.println("Please enter the number of rows [1 ~ 26]");
            rows = BookingUtils.parseIntWithConstraints(scanner.next(), 27);
        } while (rows == -1);

        int seatsPerRow;
        do {
            System.out.println("Please enter the number of seats per row [1 ~ 10]");
            seatsPerRow = BookingUtils.parseIntWithConstraints(scanner.next(), 11);
        } while (seatsPerRow == -1);

        scanner.nextLine();
        int cancellationTime;
        do {
            System.out.println("Please enter the cancellation window in minutes");
            System.out.println("Leave blank if you would like to use the default of 2 minutes");
            String input = scanner.nextLine();
            if (input.trim().isEmpty()) {
                cancellationTime = cancellationInMinutes;
            } else {
                cancellationTime = BookingUtils.parseInt(input);
            }

        } while (cancellationTime == -1);

        showService.create(new Show(showNum, rows, seatsPerRow, cancellationTime));
    }

    private void viewShowAdmin() {
        Scanner scanner = new Scanner(System.in);
        Long showNum;
        Show show = null;
        do {
            System.out.println("Please enter the show number you would like to view: ");
            showNum = BookingUtils.parseLong(scanner.next());
            if (showNum != null) {
                show = showService.getShowById(showNum);
                if (show == null) {
                    System.out.println("Show number does not exist, please try again!");
                }

            }
        } while (show == null);
        System.out.println("----------------------");
        System.out.println("Show Number: " + show.getShowNumber()
                + "\t Rows: " + show.getRows()
                + "\t Seats Per Row: " + show.getSeatsPerRow()
                + "\t Cancellation Time Window (in minutes): " + show.getCancellationTime());
        List<Booking> bookings = bookingService.getBookingsByShowNum(show.getShowNumber());
        if (!bookings.isEmpty()) {
            printBookingsAndTicketsAdmin(bookings);
        } else {
            System.out.println("There are curently no bookings for this show.");
        }
        System.out.println("----------------------");
        System.out.println();
    }

    private void viewShowsBuyer() {
        List<Show> allShows = showService.getAllShows();
        for (Show show : allShows) {
            System.out.println("----------------------");
            System.out.println("Show Number: " + show.getShowNumber()
                    + "\t Cancellation Time Window (in minutes): " + show.getCancellationTime());
            returnAvailableSeats(show);
        }
        System.out.println();
    }

    private Map<String, List<String>> returnAvailableSeats(Show show) {
        List<Booking> bookings = bookingService.getBookingsByShowNum(show.getShowNumber());
        Map<String, List<String>> seatsMap = BookingUtils.generateSeats(show.getRows(), show.getSeatsPerRow(), bookings);
        if (seatsMap != null) {
            System.out.println("Here are the available seats: ");
            seatsMap.values()
                    .forEach(System.out::println);
        } else {
            System.out.println("There are no more seats available.");
        }
        System.out.println("----------------------");
        return seatsMap;
    }

    private void printBookingsAndTicketsAdmin(List<Booking> bookings) {
        for (Booking booking : bookings) {
            System.out.println("Buyer #: " + booking.getBuyerPhone());
            getBookingDetails(booking);
        }
    }


    private void printBookingsAndTicketsBuyer(List<Booking> bookings) {
        System.out.println("----------------------");
        System.out.println("Here are the bookings tagged to the phone number entered:");
        for (Booking booking : bookings) {
            System.out.println("Show Number: " + booking.getShowNumber());
            getBookingDetails(booking);
        }
        System.out.println("----------------------");
    }

    private void getBookingDetails(Booking booking) {
        System.out.println("Booking Id: " + booking.getBookingId());
        if (BookingUtils.checkIfCurrentTimeIsAfterCancellationDateTime(clock, booking.getCancellationDateTime())) {
            System.out.println("Able to cancel tickets: No");
        } else {
            System.out.println("Able to cancel tickets: Yes");
        }
        for (Ticket ticket : booking.getTicketList()) {
            System.out.println("Seat Number: " + ticket.getSeatNumber() + ", Ticket #: " + ticket.getTicketId());
        }
    }

    @Override
    public void run(String... args) {
        displayOptions();
    }
}
