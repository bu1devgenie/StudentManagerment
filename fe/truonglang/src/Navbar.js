import {Nav, NavDropdown} from 'react-bootstrap';
import {useNavigate} from 'react-router-dom'; // Import useNavigate
import React, {useState, useEffect} from 'react';
import Cookies from 'js-cookie';

function Navbar() {
    const navigate = useNavigate(); // Initialize useNavigate
    const accessToken = Cookies.get('accessToken');
    const Name = Cookies.get('name');
    const ms = Cookies.get('ms');

    useEffect(() => {
        // Kiểm tra có AccessToken trong cookie hay không
        const accessToken = Cookies.get('accessToken');
        if (accessToken) {
            // Gọi server để kiểm tra access token
            fetch('http://localhost:9999/checkAccessToken', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + accessToken
                },
            })
                .then((response) => {
                    if (response.status === 200) {
                        // Access token is valid, proceed to fetch teachers data
                    } else {
                        throw new Error('Hết phiên đăng nhập');
                    }
                })
                .catch((error) => {
                    alert(error.message);
                    navigate('/login'); // Redirect to login page
                });
        } else {
            navigate('/login'); // Redirect to login page if access token is not available
        }
    }, [navigate, accessToken]);

    const handleSelect = (eventKey) => {
        // Use navigate to change the URL path when an item is clicked
        switch (eventKey) {
            case '1':
                navigate("/teacher");
                break;
            case '2':
                navigate("/student");
                break;
            case '3':
                navigate("/user");
                break;
            case '4':
                navigate("/account");
                break;
            case '5':
                navigate("/course");
                break;
            case '6':
                navigate("/schedule");
                break;
            case '7':
                navigate("/attendence");
                break;
            case '8':
                navigate("/weeklytable");
                break;
            case '9':
                navigate("/registerClass");
                break;
            case '10.1':
                navigate("/profile");
                break;
            case '10.2':
                navigate("/account");
                break;
            case '10.3':
                navigate("/logout");
                break;
            default:
                break;
        }
    };

    return (
        <div>
            <Nav variant="pills" activeKey="0" onSelect={handleSelect}>
                <Nav.Item>
                    <Nav.Link eventKey="0">
                        Trường Làng
                    </Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link eventKey="1">
                        Teacher
                    </Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link eventKey="2">
                        Student
                    </Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link eventKey="3">
                        User
                    </Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link eventKey="4">
                        Account
                    </Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link eventKey="5">
                        Course
                    </Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link eventKey="6">
                        Schedule
                    </Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link eventKey="7">
                        Attendance
                    </Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link eventKey="8">
                        Weekly timetable
                    </Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link eventKey="9">
                        Register Class
                    </Nav.Link>
                </Nav.Item>

                <div style={{marginLeft: 'auto'}}>
                    <NavDropdown title={Name} id="nav-dropdown">
                        <NavDropdown.Item eventKey="10.1">Profile</NavDropdown.Item>
                        <NavDropdown.Item eventKey="10.2">Account</NavDropdown.Item>
                        <NavDropdown.Divider />
                        <NavDropdown.Item eventKey="10.3">Logout</NavDropdown.Item>
                    </NavDropdown>
                </div>
            </Nav>

        </div>
    );
}

export default Navbar;
