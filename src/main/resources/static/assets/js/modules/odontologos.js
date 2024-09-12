import { getData, postData, putData, deleteData } from '../utils/api.js';
import { clearForm, showAlert } from '../utils/formUtils.js';

document.getElementById("formRegistrarOdontologo").addEventListener("submit", function(event) {
    event.preventDefault();

    const nombre = document.getElementById("nombre").value;
    const apellido = document.getElementById("apellido").value;
    const matricula = document.getElementById("matricula").value;

    postData('odontologos/registrar', { nombre, apellido, matricula })
        .then(() => {
            showAlert('Odontólogo registrado exitosamente');
            actualizarListaOdontologos();
            clearForm('formRegistrarOdontologo');
        });
});

function actualizarListaOdontologos() {
    getData('odontologos/listar')
        .then(odontologos => {
            const tbody = document.getElementById("listaOdontologos").querySelector("tbody");
            tbody.innerHTML = '';

            if (odontologos.length === 0) {
                tbody.innerHTML = '<tr><td colspan="5">No hay odontólogos registrados</td></tr>';
            } else {
                odontologos.forEach(odontologo => {
                    const fila = `<tr>
                        <td>${odontologo.nombre}</td>
                        <td>${odontologo.apellido}</td>
                        <td>${odontologo.matricula}</td>
                        <td><button class="eliminar" data-id="${odontologo.id}">Eliminar</button></td>
                    </tr>`;
                    tbody.innerHTML += fila;
                });
            }
        })
        .catch(error => console.error('Error al listar los odontólogos:', error));
}

document.addEventListener("DOMContentLoaded", actualizarListaOdontologos);

document.getElementById("listaOdontologos").addEventListener("click", function(event) {
    if (event.target.tagName === 'BUTTON' && event.target.classList.contains("eliminar")) {
        const odontologoId = event.target.dataset.id;
        eliminarOdontologo(odontologoId);
    }
});

document.getElementById("btnEditarOdontologo").addEventListener("click", function() {
    const id = document.getElementById("buscarOdontologo").value;
    if (id) {
        actualizarOdontologo(id);
    } else {
        showAlert('Debes buscar un odontologo antes de editar.');
    }
});

function buscarOdontologo() {
    const id = document.getElementById("buscarOdontologo").value;

    if (!id) {
        showAlert('Por favor, ingresa un ID válido');
        return;
    }

    getData(`odontologos/${id}`)
        .then(odontologo => {
            if (odontologo) {
                document.getElementById("nombre").value = odontologo.nombre;
                document.getElementById("apellido").value = odontologo.apellido;
                document.getElementById("matricula").value = odontologo.matricula;
            } else {
                showAlert('Odontólogo no encontrado');
                clearForm('formRegistrarOdontologo');
            }
        })
        .catch(error => {
            console.error('Error al buscar el odontólogo:', error);
            showAlert('Error al buscar el odontólogo');
            clearForm('formRegistrarOdontologo');
        });
}

document.getElementById("btnBuscarOdontologo").addEventListener("click", buscarOdontologo);

function actualizarOdontologo(id) {
    const nombre = document.getElementById("nombre").value;
    const apellido = document.getElementById("apellido").value;
    const matricula = document.getElementById("matricula").value;

    const odontologo = {
        nombre: nombre,
        apellido: apellido,
        matricula: matricula
    };

    putData(`odontologos/actualizar/${id}`, odontologo)
        .then(() => {
            showAlert('Odontólogo actualizado exitosamente');
            actualizarListaOdontologos();
            clearForm('formRegistrarOdontologo');
        })
        .catch(error => console.error('Error al actualizar el odontólogo:', error));
}

function eliminarOdontologo(id) {
    if (confirm("¿Estás seguro de que deseas eliminar este odontólogo?")) {
        deleteData(`odontologos/eliminar?id=${id}`)
            .then(() => {
                showAlert('Odontólogo eliminado correctamente');
                actualizarListaOdontologos();
            })
            .catch(error => console.error('Error al eliminar el odontólogo:', error));
    }
}
