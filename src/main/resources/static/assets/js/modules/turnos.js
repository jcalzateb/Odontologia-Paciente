import { getData, postData, putData, deleteData } from '../utils/api.js';
import { clearForm, showAlert } from '../utils/formUtils.js';

document.getElementById("formRegistrarTurno").addEventListener("submit", function(event) {
    event.preventDefault();

    const pacienteId = document.getElementById("pacienteId").value;
    const odontologoId = document.getElementById("odontologoId").value;
    const fechaTurno = document.getElementById("fechaTurno").value;

    const turno = {
        pacienteId: parseInt(pacienteId),
        odontologoId: parseInt(odontologoId),
        fechaTurno: fechaTurno
    };

    postData('turnos/registrar', turno)
        .then(response => {
            showAlert('Turno registrado exitosamente');
            actualizarListaTurnos();
            clearForm('formRegistrarTurno');
        })
        .catch(error => {
            console.error('Error al registrar el turno:', error);
            showAlert('Error al registrar el turno. Verifica los datos e intenta nuevamente.');
        });
});

function actualizarListaTurnos() {
    getData('turnos/listar')
        .then(turnos => {
            const tbody = document.getElementById("listaTurnos").querySelector("tbody");
            tbody.innerHTML = '';

            if (turnos.length === 0) {
                tbody.innerHTML = '<tr><td colspan="5">No hay turnos registrados</td></tr>';
            } else {
                turnos.forEach(turno => {
                    const fila = `<tr>
                        <td>${turno.pacienteSalidaDto.nombre} ${turno.pacienteSalidaDto.apellido}</td>
                        <td>${turno.odontologoSalidaDto.nombre} ${turno.odontologoSalidaDto.apellido}</td>
                        <td>${turno.fechaTurno}</td>
                        <td><button class="eliminar" data-id="${turno.id}">Eliminar</button></td>
                    </tr>`;
                    tbody.innerHTML += fila;
                });
            }
        })
        .catch(error => console.error('Error al listar los turnos:', error));
}

document.addEventListener("DOMContentLoaded", actualizarListaTurnos);

document.getElementById("listaTurnos").addEventListener("click", function(event) {
    if (event.target.tagName === 'BUTTON' && event.target.classList.contains("eliminar")) {
        const turnosId = event.target.dataset.id;
        eliminarTurno(turnosId);
    }
});

document.getElementById("btnEditarTurno").addEventListener("click", function() {
    const id = document.getElementById("buscarTurno").value;
    if (id) {
        actualizarTurno(id);
    } else {
        showAlert('Debes buscar un turno antes de editar.');
    }
});

function buscarTurno() {
    const id = document.getElementById("buscarTurno").value;

    if (!id) {
        showAlert('Por favor, ingresa un ID válido');
        return;
    }

    getData(`turnos/${id}`)
        .then(turno => {
            if (turno) {
                document.getElementById("pacienteId").value = turno.pacienteSalidaDto.id;
                document.getElementById("odontologoId").value = turno.odontologoSalidaDto.id;
                document.getElementById("fechaTurno").value = turno.fechaTurno;
            } else {
                showAlert('Turno no encontrado');
                clearForm('formRegistrarTurno');
            }
        })
        .catch(error => {
            console.error('Error al buscar el turno:', error);
            showAlert('Error al buscar el turno');
            clearForm('formRegistrarTurno');
        });
}

document.getElementById("btnBuscarTurno").addEventListener("click", buscarTurno);

function actualizarTurno(id) {
    const pacienteId = document.getElementById("pacienteId").value;
    const odontologoId = document.getElementById("odontologoId").value;
    const fechaTurno = document.getElementById("fechaTurno").value;

    const turno = {
        pacienteId: parseInt(pacienteId),
        odontologoId: parseInt(odontologoId),
        fechaTurno: fechaTurno
    };

    putData(`turnos/actualizar/${id}`, turno)
        .then(() => {
            showAlert('Turno actualizado exitosamente');
            actualizarListaTurnos();
            clearForm('formRegistrarTurno');
        })
        .catch(error => console.error('Error al actualizar el turno:', error));
}

function eliminarTurno(id) {
    if (confirm("¿Estás seguro de que deseas eliminar este turno?")) {
        deleteData(`turnos/eliminar?id=${id}`)
            .then(() => {
                showAlert('Turno eliminado correctamente');
                actualizarListaTurnos();
            })
            .catch(error => console.error('Error al eliminar el turno:', error));
    }
}

