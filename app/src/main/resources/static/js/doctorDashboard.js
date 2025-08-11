/*
  This script handles the doctor dashboard functionality:
  - Loads and displays scheduled appointments
  - Allows filtering by patient name or date
*/

document.addEventListener("DOMContentLoaded", () => {
  renderContent();
  loadAppointments();

  document.getElementById("searchBar").addEventListener("input", onSearchInput);
  document.getElementById("todayButton").addEventListener("click", onTodayButtonClick);
  document.getElementById("datePicker").addEventListener("change", onDatePickerChange);
});

let selectedDate = getToday();
let patientName = null;

function getToday() {
  const d = new Date();
  return d.toISOString().split("T")[0];
}

function onSearchInput(event) {
  const value = event.target.value.trim();
  patientName = value ? value : null;
  loadAppointments();
}

function onTodayButtonClick() {
  selectedDate = getToday();
  document.getElementById("datePicker").value = selectedDate;
  loadAppointments();
}

function onDatePickerChange(event) {
  selectedDate = event.target.value;
  loadAppointments();
}

async function loadAppointments() {
  const token = localStorage.getItem("token");
  const tbody = document.getElementById("patientTableBody");
  tbody.innerHTML = "";

  try {
    const appointments = await getAllAppointments(selectedDate, patientName, token);

    if (!appointments || appointments.length === 0) {
      tbody.innerHTML = `<tr><td colspan="5">No Appointments found for today.</td></tr>`;
    } else {
      appointments.forEach(app => {
        const patient = {
          id: app.patientId,
          name: app.patientName,
          phone: app.patientPhone,
          email: app.patientEmail
        };
        tbody.appendChild(createPatientRow(patient, app));
      });
    }
  } catch (err) {
    tbody.innerHTML = `<tr><td colspan="5">Error loading appointments. Try again later.</td></tr>`;
  }
}

// Placeholder API/service functions
async function getAllAppointments(date, patientName, token) {
  const url = `/appointments/${date || "null"}/${patientName || "null"}/${token}`;
  const response = await fetch(url);
  if (!response.ok) throw new Error("Error fetching appointments");
  return await response.json();
}

// Example row generator
function createPatientRow(patient, appointment) {
  const tr = document.createElement("tr");
  tr.innerHTML = `
    <td>${patient.id}</td>
    <td>${patient.name}</td>
    <td>${patient.phone}</td>
    <td>${patient.email}</td>
    <td>
      <button onclick="viewPrescription(${appointment.id})">View</button>
    </td>
  `;
  return tr;
}

// Example prescription viewer (implement as needed)
function viewPrescription(appointmentId) {
  // Open modal and fetch prescription info for the appointmentId
  alert("Prescription view for appointment: " + appointmentId);
}