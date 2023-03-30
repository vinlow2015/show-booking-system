package com.booking.system.showbookingsystem.utils;

import com.booking.system.showbookingsystem.entity.Booking;
import com.booking.system.showbookingsystem.entity.Ticket;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class BookingUtils {

    private static final ZoneId systemZoneId = ZoneId.of("Asia/Singapore");

    public static boolean checkIfValidPhoneNumber(String input) {
        if (input.length() != 8) {
            return false;
        }
        for (char c : input.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public static Long parseLong(String input) {
        Long longInput = null;
        try {
            longInput = Long.parseLong(input.trim());
        } catch (NumberFormatException e) {
            String errorMessage = "Invalid Long input for field detected, please try again!";
            System.out.println(errorMessage);
        }
        return longInput;
    }

    public static List<String> parseCommaSeparatedString(String input) {
        List<String> stringList = null;
        if (input == null) {
            return null;
        }
        try {
            stringList = Arrays.stream(input.trim().split(",")).map(String::trim).toList();
        } catch (NumberFormatException e) {
            String errorMessage = "Invalid comma separated input for field detected, please try again!";
            System.out.println(errorMessage);
        }
        return stringList;
    }

    public static int parseIntWithConstraints(String input, int upperBound) {
        int intInput = -1;
        try {
            intInput = Integer.parseInt(input.trim());
            if (intInput <= 0 || intInput > upperBound) {
                System.out.println("Input is beyond the constraints allowed");
                intInput = -1;
            }
        } catch (NumberFormatException e) {
            String errorMessage = "Invalid Integer input for field detected, please try again!";
            System.out.println(errorMessage);
        }
        return intInput;
    }

    public static boolean checkIfSeatsToBookAreValid(Map<String, List<String>> seatsMap, List<String> seatsToBook) {
        int count = 0;
        for (String seat : seatsToBook) {
            char seatRowChar = seat.charAt(0);
            String seatRowString = Character.toString(seatRowChar).toUpperCase();
            if (!Character.isLetter(seatRowChar) || !seatsMap.containsKey(seatRowString)) {
                return false;
            }
            List<String> seatsRow = seatsMap.get(seatRowString);
            if (!seatsRow.contains(seat)) {
                return false;
            } else {
                count++;
            }
        }
        return count == seatsToBook.size();
    }

    public static Map<String, List<String>> generateSeats(int rows, int seatsPerRow, List<Booking> bookingList) {
        int startingCharNum = 65;
        Map<String, List<String>> seatsMap = new LinkedHashMap<>();
        List<String> bookedSeats = getSeatNumbersFromBookings(bookingList);
        int seatsBooked = 0;
        for (int i = 0; i < rows; i++) {
            String rowChar = Character.toString((char) startingCharNum);
            for (int j = 1; j <= seatsPerRow; j++) {
                List<String> currentRow = new ArrayList<>();
                String seat = rowChar + j;

                if (seatsMap.containsKey(rowChar)) {
                    currentRow = seatsMap.get(rowChar);
                }
                if (!bookedSeats.contains(seat)) {
                    currentRow.add(seat);
                } else {
                    seatsBooked++;
                    currentRow.add("x");
                }
                seatsMap.put(rowChar, currentRow);
            }
            startingCharNum += 1;
        }
        if (seatsBooked == seatsPerRow * rows) {
            return null;
        }
        return seatsMap;
    }

    private static List<String> getSeatNumbersFromBookings(List<Booking> bookingList) {
        List<Ticket> ticketList = getAllTicketsFromBookingList(bookingList);
        return ticketList.stream().map(Ticket::getSeatNumber).toList();
    }

    private static List<Ticket> getAllTicketsFromBookingList(List<Booking> bookingList) {
        List<Ticket> ticketList;
        ticketList = bookingList.stream()
                .map(Booking::getTicketList)
                .flatMap(List::stream)
                .toList();
        return ticketList;
    }

    public static boolean checkIfTicketNumberExistsWithinBookings(String ticketId, List<Booking> bookingList) {
        List<Ticket> ticketList = getAllTicketsFromBookingList(bookingList);
        return ticketList.stream().anyMatch(ticket -> ticket.getTicketId().equals(ticketId));
    }

    public static int parseInt(String input) {
        int intInput = -1;
        try {
            intInput = Integer.parseInt(input.trim());
            if (intInput <= 0) {
                System.out.println("Input is beyond the constraints allowed");
                intInput = -1;
            }
        } catch (NumberFormatException e) {
            String errorMessage = "Invalid Integer input for field detected, please try again!";
            System.out.println(errorMessage);
        }
        return intInput;
    }

    public static List<Ticket> removeSpecificTicketFromList(List<Ticket> ticketList, Ticket ticket) {
        ticketList.remove(ticket);
        return ticketList;
    }

    public static void showExitMessage() {
        System.out.println("Your choices have been saved!");
        System.out.println("See you soon!");
    }

    public static String generateBookingId() {
        return UUID.randomUUID().toString();
    }

    public static List<Ticket> generateTicketsFromSeats(String bookingId, List<String> seatList) {
        return seatList.stream()
                .map(seat -> new Ticket(UUID.randomUUID().toString(), bookingId, seat))
                .toList();
    }

    public static LocalDateTime getCancellationDateTime(Clock clock, int minutes) {
        return LocalDateTime.ofInstant(clock.instant().plus(minutes, ChronoUnit.MINUTES), systemZoneId);
    }

    public static boolean checkIfCurrentTimeIsAfterCancellationDateTime(Clock clock, LocalDateTime cancellationDateTime) {
        return clock.instant().isAfter(cancellationDateTime.atZone(systemZoneId).toInstant());
    }
}
