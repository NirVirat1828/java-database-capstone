// index.js

document.addEventListener("DOMContentLoaded", function () {
    // Role selection button event handlers
    document.getElementById("admin-btn").addEventListener("click", function () {
        // Open Admin login modal or redirect to admin login page
        // Example: Show modal
        if (typeof openModal === "function") {
            openModal('adminLogin');
        } else {
            alert("Admin login modal not implemented!");
        }
    });

    document.getElementById("patient-btn").addEventListener("click", function () {
        // Open Patient login modal or redirect to patient login page
        if (typeof openModal === "function") {
            openModal('patientLogin');
        } else {
            alert("Patient login modal not implemented!");
        }
    });

    document.getElementById("doctor-btn").addEventListener("click", function () {
        // Open Doctor login modal or redirect to doctor login page
        if (typeof openModal === "function") {
            openModal('doctorLogin');
        } else {
            alert("Doctor login modal not implemented!");
        }
    });
});

// If you want to wire up modal close and other logic, you can do it here as well