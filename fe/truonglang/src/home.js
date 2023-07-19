import React from 'react';
import { Nav, Navbar, NavDropdown } from 'react-bootstrap';


function Home() {
  return (
    <div>
      <Navbar bg="light" expand="lg">
        <Navbar.Brand href="#home">Trang chủ</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="mr-auto">
            <Nav.Link href="#students">Students</Nav.Link>
            <Nav.Link href="#teachers">Teachers</Nav.Link>
            <NavDropdown title="User" id="basic-nav-dropdown">
              <NavDropdown.Item href="#profile">Profile</NavDropdown.Item>
              <NavDropdown.Item href="#settings">Settings</NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item href="#logout">Logout</NavDropdown.Item>
            </NavDropdown>
            <Nav.Link href="#account">Account</Nav.Link>
            <Nav.Link href="#semester">Semester</Nav.Link>
            <Nav.Link href="#course">Course</Nav.Link>
            <Nav.Link href="#class">Class</Nav.Link>
            <Nav.Link href="#room">Room</Nav.Link>
            <Nav.Link href="#schedule">Schedule</Nav.Link>
          </Nav>
        </Navbar.Collapse>
      </Navbar>
      <div>
        <h2>Trang chủ</h2>
        <p>Chào mừng bạn đến trang chủ!</p>
      </div>
    </div>
  );
}

export default Home;
