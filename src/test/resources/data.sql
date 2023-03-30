INSERT INTO SHOW (show_number, rows, seats_per_row, cancellation_time)
VALUES ('1', '1', '1', '1');
INSERT INTO SHOW (show_number, rows, seats_per_row, cancellation_time)
VALUES ('2', '10', '10', '1');

INSERT INTO BOOKING(booking_id, show_number, buyer_phone, cancellation_date_time)
VALUES ('f8c3de3d-1fea-4d7c-a8b0-29f63cssss', '1', '12345678', '2025-03-25 12:26:34.438754');
INSERT INTO TICKET(ticket_id, booking_id, seat_number)
VALUES ('f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454', 'f8c3de3d-1fea-4d7c-a8b0-29f63cssss', 'A1');
INSERT INTO BOOKING_TICKET_LIST(booking_booking_id, ticket_list_ticket_id)
VALUES ('f8c3de3d-1fea-4d7c-a8b0-29f63cssss', 'f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454');