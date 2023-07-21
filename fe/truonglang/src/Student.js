import React from 'react';
import Navbar from './Navbar'; // Import the Navbar component
import Table from 'react-bootstrap/Table';
import Button from 'react-bootstrap/Button';



import 'bootstrap/dist/css/bootstrap.min.css';


function Student() {
    return (
        <div >
            <Navbar />
            <Table className="custom-table" striped bordered hover size="sm">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Mssv</th>
                        <th>Semester</th>
                        <th>Name</th>
                        <th>DOB</th>
                        <th>Address</th>
                        <th>Avatar</th>
                        <th>Email</th>
                        <th>Gender</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>1</td>
                        <td>Mark</td>
                        <td>Otto</td>
                        <td>@mdo</td>
                        <td>@mdo</td>
                        <td>@mdo</td>
                        <td>@mdo</td>
                        <td>@mdo</td>
                        <td>@mdo</td>
                        <td>
                            <Button variant="primary">Edit</Button>{' '}
                            <Button variant="danger">Delete</Button>{' '}
                        </td>
                    </tr>
                    <tr>
                        <td>1</td>
                        <td>Mark</td>
                        <td>Otto</td>
                        <td>@mdo</td>
                        <td>@mdo</td>
                        <td>@mdo</td>
                        <td>@mdo</td>
                        <td>@mdo</td>
                        <td>@mdo</td>
                        <td>
                            <Button variant="primary">Edit</Button>{' '}
                            <Button variant="danger">Delete</Button>{' '}
                        </td>
                    </tr>
                    <tr>
                        <td>1</td>
                        <td>Mark</td>
                        <td>Otto</td>
                        <td>@mdo</td>
                        <td>@mdo</td>
                        <td>@mdo</td>
                        <td>@mdo</td>
                        <td>@mdo</td>
                        <td>@mdo</td>
                        <td style={{alignItems: "center"}}>
                            <Button variant="primary">Edit</Button>{' '}
                            <Button variant="danger">Delete</Button>{' '}
                        </td>
                    </tr>
                </tbody>
            </Table>
        </div>
    );
}

export default Student;
