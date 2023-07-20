import React from 'react';
import Navbar from './Navbar'; // Import the Navbar component
import Table from 'react-bootstrap/Table';
import './Home.css';
import Button from 'react-bootstrap/Button';

function Home() {
  return (
    <div className="home-container">
      <div className='mt-3'></div>
      <Navbar /> {/* Render the Navbar component */}
      <br></br>
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
            <td style={{alignItems : "center"}}>
              <Button variant="primary">Edit</Button>{' '}
              <Button variant="danger">Delete</Button>{' '}
            </td>
          </tr>
        </tbody>
      </Table>
    </div>
  );
}

export default Home;
