import { getData, postData, putData, deleteData } from '../utils/api.js';
import { clearForm, showAlert } from '../utils/formUtils.js';

document.getElementById("formRegistrarPaciente").addEventListener("submit", function(event) {
    event.preventDefault();

    const nombre = document.getElementById("nombre").value;
    const apellido = document.getElementById("apellido").value;
    const dni = document.getElementById("dni").value;
    const fechaIngreso = document.getElementById("fechaIngreso").value;

    const calle = document.getElementById("calle").value;
    const numero = document.getElementById("numero").value;
    const localidad = document.getElementById("localidad").value;
    const provincia = document.getElementById("provincia").value;

    const paciente = {
        nombre: nombre,
        apellido: apellido,
        dni: parseInt(dni),
        fechaIngreso: fechaIngreso,
        domicilioEntradaDto: {
            calle: calle,
            numero: parseInt(numero),
            localidad: localidad,
            provincia: provincia
        }
    };

    postData('pacientes/registrar', paciente)
        .then(() => {
            showAlert('Paciente registrado exitosamente');
            actualizarListaPacientes();
            clearForm('formRegistrarPaciente');
        })
        .catch(error => console.error('Error en el registro del paciente:', error));
});

function actualizarListaPacientes() {
    getData('pacientes/listar')
        .then(pacientes => {
            const tbody = document.getElementById("listaPacientes").querySelector("tbody");
            tbody.innerHTML = '';

            if (pacientes.length === 0) {
                tbody.innerHTML = '<tr><td colspan="6">No hay pacientes registrados</td></tr>';
            } else {
                pacientes.forEach(paciente => {
                    const fila = `<tr>
                        <td>${paciente.nombre}</td>
                        <td>${paciente.apellido}</td>
                        <td>${paciente.dni}</td>
                        <td>${paciente.fechaIngreso}</td>
                        <td>${paciente.domicilioSalidaDto.calle} ${paciente.domicilioSalidaDto.numero}, ${paciente.domicilioSalidaDto.localidad}, ${paciente.domicilioSalidaDto.provincia}</td>
                        <td><button class="eliminar" data-id="${paciente.id}">Eliminar</button></td>
                    </tr>`;
                    tbody.innerHTML += fila;
                });
            }
        })
        .catch(error => console.error('Error al listar los pacientes:', error));
}

document.addEventListener("DOMContentLoaded", actualizarListaPacientes);

document.getElementById("listaPacientes").addEventListener("click", function(event) {
    if (event.target.tagName === 'BUTTON' && event.target.classList.contains("eliminar")) {
        const pacienteId = event.target.dataset.id;
        eliminarPaciente(pacienteId);
    }
});

document.getElementById("btnEditarPaciente").addEventListener("click", function() {
    const id = document.getElementById("buscarPaciente").value;
    if (id) {
        actualizarPaciente(id);
    } else {
        showAlert('Debes buscar un paciente antes de editar.');
    }
});

function buscarPaciente() {
    const id = document.getElementById("buscarPaciente").value;

    if (!id) {
        showAlert('Por favor, ingresa un ID válido');
        return;
    }

    getData(`pacientes/${id}`)
        .then(paciente => {
            if (paciente) {
                document.getElementById("nombre").value = paciente.nombre;
                document.getElementById("apellido").value = paciente.apellido;
                document.getElementById("dni").value = paciente.dni;
                document.getElementById("fechaIngreso").value = paciente.fechaIngreso;

                document.getElementById("calle").value = paciente.domicilioSalidaDto.calle;
                document.getElementById("numero").value = paciente.domicilioSalidaDto.numero;
                document.getElementById("localidad").value = paciente.domicilioSalidaDto.localidad;
                document.getElementById("provincia").value = paciente.domicilioSalidaDto.provincia;
            } else {
                showAlert('Paciente no encontrado');
                clearForm('formRegistrarPaciente');
            }
        })
        .catch(error => {
            console.error('Error al buscar el paciente:', error);
            showAlert('Error al buscar el paciente');
            clearForm('formRegistrarPaciente');
        });
}

document.getElementById("btnBuscarPaciente").addEventListener("click", buscarPaciente);

function actualizarPaciente(id) {
    const nombre = document.getElementById("nombre").value;
    const apellido = document.getElementById("apellido").value;
    const dni = document.getElementById("dni").value;
    const fechaIngreso = document.getElementById("fechaIngreso").value;

    const calle = document.getElementById("calle").value;
    const numero = document.getElementById("numero").value;
    const localidad = document.getElementById("localidad").value;
    const provincia = document.getElementById("provincia").value;

    const paciente = {
        nombre: nombre,
        apellido: apellido,
        dni: parseInt(dni),
        fechaIngreso: fechaIngreso,
        domicilioEntradaDto: {
            calle: calle,
            numero: parseInt(numero),
            localidad: localidad,
            provincia: provincia
        }
    };

    putData(`pacientes/actualizar/${id}`, paciente)
        .then(() => {
            showAlert('Paciente actualizado exitosamente');
            actualizarListaPacientes();
            clearForm('formRegistrarPaciente');
        })
        .catch(error => console.error('Error al actualizar el paciente:', error));
}


function eliminarPaciente(id) {
    if (confirm("¿Estás seguro de que deseas eliminar este paciente?")) {
        deleteData(`pacientes/eliminar?id=${id}`)
            .then(() => {
                showAlert('Paciente eliminado correctamente');
                actualizarListaPacientes();
            })
            .catch(error => console.error('Error al eliminar el paciente:', error));
    }
}
