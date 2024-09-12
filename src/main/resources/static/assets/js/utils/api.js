const API_URL = 'http://localhost:8080';

export function getData(endpoint) {
    return fetch(`${API_URL}/${endpoint}`)
        .then(response => response.json())
        .catch(error => console.error('Error en la petición GET:', error));
}

export function postData(endpoint, data) {
    return fetch(`${API_URL}/${endpoint}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .catch(error => console.error('Error en la petición POST:', error));
}

export function putData(endpoint, data) {
  return fetch(`${API_URL}/${endpoint}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(data)
  })
    .then(response => {
      if (!response.ok) throw new Error('Error en la petición PUT');
      return response.json();
    })
    .catch(error => console.error('Error en la petición PUT:', error));
}

export function deleteData(endpoint) {
    return fetch(`${API_URL}/${endpoint}`, {
        method: 'DELETE'
    })
    .then(response => {
        if (!response.ok) throw new Error('Error en la petición DELETE');
        return response.json();
    })
    .catch(error => console.error('Error en la petición DELETE:', error));
}
