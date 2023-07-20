import React from 'react';
import {Routes, Route} from 'react-router-dom';
import Navbar from './Navbar'; // Import the Navbar component


function Teacher() {
    return (
        <div>
            <Navbar /> {/* Render the Navbar component */}
            <div className="body">
            </div>
        </div>
    );
}

export default Teacher;
