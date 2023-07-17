import React from 'react';
import { Link } from 'react-router-dom';

function Home() {
  return (
    <div>
      <nav>
        <ul>
          <li>
            <Link to="/student">Student</Link>
          </li>
          <li>
            <Link to="/teacher">Teacher</Link>
          </li>
          <li>
            <Link to="/user">User</Link>
          </li>
          <li>
            <Link to="/account">Account</Link>
          </li>
        </ul>
      </nav>
      <h1>Welcome to the Home Page!</h1>
    </div>
  );
}

export default Home;