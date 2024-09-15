declare module "../generated/org/vaadin/marcus/service/BookingDetails" {
    export interface BookingDetails {
        bookingNumber: string;
        firstName: string;
        lastName: string;
        date: string;
        from: string;
        to: string;
        bookingStatus: string;
        bookingClass: string;
    }

    export default BookingDetails;
}