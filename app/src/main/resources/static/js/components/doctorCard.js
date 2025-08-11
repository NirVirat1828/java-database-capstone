// doctorCard.js

// Import dependencies (assumes these modules exist and are in correct path)
import { openBookingOverlay } from "../loggedPatient.js";
import { deleteDoctor } from "../services/doctorServices.js";
import { getPatientDetails } from "../services/patientServices.js";

/**
 * Creates a doctor card DOM element.
 * @param {Object} doctor - Doctor object with details.
 * @returns {HTMLElement} The doctor card element.
 */
export function createDoctorCard(doctor) {
  // Main card container
  const card = document.createElement("div");
  card.className = "doctor-card";

  // User role detection
  const userRole = localStorage.getItem("userRole");

  // Info section
  const infoDiv = document.createElement("div");
  infoDiv.className = "doctor-info";

  // Doctor name
  const name = document.createElement("h3");
  name.textContent = doctor.name;
  infoDiv.appendChild(name);

  // Specialization
  const spec = document.createElement("p");
  spec.textContent = `Specialization: ${doctor.specialty || doctor.specialization || "General"}`;
  infoDiv.appendChild(spec);

  // Email
  const email = document.createElement("p");
  email.textContent = `Email: ${doctor.email}`;
  infoDiv.appendChild(email);

  // Available times
  const times = document.createElement("p");
  times.textContent = `Available Times: ${Array.isArray(doctor.availableTimes) ? doctor.availableTimes.join(", ") : (doctor.availableTimes || "Not specified")}`;
  infoDiv.appendChild(times);

  // Actions section
  const actionsDiv = document.createElement("div");
  actionsDiv.className = "doctor-actions";

  // === ADMIN ROLE: Delete doctor ===
  if (userRole === "admin") {
    const delBtn = document.createElement("button");
    delBtn.textContent = "Delete";
    delBtn.className = "delete-btn";
    delBtn.onclick = async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        alert("Admin authentication required.");
        return;
      }
      const confirmed = confirm(`Delete Dr. ${doctor.name}?`);
      if (!confirmed) return;
      const result = await deleteDoctor(doctor.id, token);
      alert(result.message);
      if (result.success) card.remove();
    };
    actionsDiv.appendChild(delBtn);
  }
  // === PATIENT NOT LOGGED IN ===
  else if (userRole === "patient" || !userRole) {
    const bookBtn = document.createElement("button");
    bookBtn.textContent = "Book Now";
    bookBtn.className = "book-btn";
    bookBtn.onclick = () => alert("Please log in as a patient to book an appointment.");
    actionsDiv.appendChild(bookBtn);
  }
  // === PATIENT LOGGED-IN ===
  else if (userRole === "loggedPatient") {
    const bookBtn = document.createElement("button");
    bookBtn.textContent = "Book Now";
    bookBtn.className = "book-btn";
    bookBtn.onclick = async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        alert("Please log in as a patient.");
        window.location.href = "/";
        return;
      }
      // Fetch patient details
      const patient = await getPatientDetails(token);
      if (!patient) {
        alert("Could not fetch patient details. Please re-login.");
        return;
      }
      // Show booking overlay
      openBookingOverlay(doctor, patient);
    };
    actionsDiv.appendChild(bookBtn);
  }

  // Assemble card
  card.appendChild(infoDiv);
  card.appendChild(actionsDiv);

  return card;
}