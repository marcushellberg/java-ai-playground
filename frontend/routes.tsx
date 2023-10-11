import CustomerServiceView from 'Frontend/views/customerservice/CustomerServiceView';
import MainLayout from 'Frontend/views/MainLayout.js';
import { createBrowserRouter, RouteObject } from 'react-router-dom';
import BookingsView from "Frontend/views/bookings/BookingsView";


export const routes: RouteObject[] = [
  {
    element: <MainLayout />,
    handle: { title: 'Main' },
    children: [
      { path: '/', element: <CustomerServiceView />, handle: { title: 'Customer Service' } },
      { path: '/bookings', element: <BookingsView />, handle: { title: 'Bookings' } },
    ],
  },
];

export default createBrowserRouter(routes);
