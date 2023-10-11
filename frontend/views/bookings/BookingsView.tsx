import {useEffect, useState} from "react";
import {Grid} from "@hilla/react-components/Grid";
import {GridColumn} from "@hilla/react-components/GridColumn";
import BookingInfo from "Frontend/generated/com/example/application/client/BookingService/BookingInfo";
import {BookingService} from "Frontend/generated/endpoints";

export default function BookingsView() {
    const [bookings, setBookings] = useState<BookingInfo[]>([]);

    useEffect(() => {
        BookingService.getBookings().then(setBookings);
    }, []);

    return (
        <div className="flex flex-col h-full">
            <Grid items={bookings}>
                <GridColumn path="bookingNumber"/>
                <GridColumn path="firstName"/>
                <GridColumn path="lastName"/>
                <GridColumn path="bookingFrom"/>
                <GridColumn path="bookingTo"/>
                <GridColumn path="bookingStatus"/>
            </Grid>
        </div>
    );
}
