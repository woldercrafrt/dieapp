// Configuración de la API
const API_BASE_URL = 'http://localhost:8080/api';

// Función para obtener headers con token JWT
function getAuthHeaders() {
    const token = localStorage.getItem('authToken');
    const headers = {
        'Content-Type': 'application/json'
    };
    
    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }
    
    return headers;
}

// Estado global de la aplicación
let currentUser = null;
let maletines = [];
let discos = [];
let usuarios = [];
let currentSection = 'dashboard';

// Utilidades
function formatDate(dateString) {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleDateString('es-ES', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
    });
}

function formatDateTime(dateString) {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleString('es-ES', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
    });
}

function showNotification(message, type = 'info') {
    // Crear notificación
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.textContent = message;
    
    // Agregar al DOM
    document.body.appendChild(notification);
    
    // Mostrar con animación
    setTimeout(() => notification.classList.add('show'), 100);
    
    // Remover después de 3 segundos
    setTimeout(() => {
        notification.classList.remove('show');
        setTimeout(() => document.body.removeChild(notification), 300);
    }, 3000);
}

// Inicialización de la aplicación
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
});

function initializeApp() {
    checkAuthentication();
    setupEventListeners();
    setupNavigation();
    setupModals();
}

// Verificar autenticación
function checkAuthentication() {
    // Si estamos en la página de login, no verificar autenticación
    if (window.location.pathname.includes('login.html')) {
        return;
    }
    
    const token = localStorage.getItem('authToken');
    if (!token) {
        window.location.href = 'login.html';
        return;
    }
    
    fetch(`${API_BASE_URL}/auth/current-user`, {
        method: 'GET',
        headers: getAuthHeaders()
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error('No autenticado');
        }
    })
    .then(data => {
        if (data.success && data.user) {
            currentUser = data.user;
            // Extraer solo la parte antes del @ del email
            const username = data.user.email ? data.user.email.split('@')[0] : 'Usuario';
            document.getElementById('userEmail').textContent = username;
            loadDashboardData();
        } else {
            throw new Error('Respuesta inválida del servidor');
        }
    })
    .catch(error => {
        // Solo mostrar error si no estamos en login
        if (!window.location.pathname.includes('login.html')) {
            console.error('Error de autenticación:', error);
            // Limpiar token inválido
            localStorage.removeItem('authToken');
            localStorage.removeItem('userEmail');
            showNotification('Sesión expirada. Redirigiendo al login...', 'error');
            setTimeout(() => {
                window.location.href = 'login.html';
            }, 2000);
        }
    });
}

// Configurar event listeners
function setupEventListeners() {
    // Logout button
    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', logout);
    }
    
    // Botones de agregar
    const addMaletinBtn = document.getElementById('addMaletinBtn');
    if (addMaletinBtn) {
        addMaletinBtn.addEventListener('click', () => openMaletinModal());
    }
    
    const addDiscoBtn = document.getElementById('addDiscoBtn');
    if (addDiscoBtn) {
        addDiscoBtn.addEventListener('click', () => openDiscoModal());
    }
    
    const addUsuarioBtn = document.getElementById('addUsuarioBtn');
    if (addUsuarioBtn) {
        addUsuarioBtn.addEventListener('click', () => openUsuarioModal());
    }
    
    // Filtros
    const filterMaletinesBtn = document.getElementById('filterMaletinesBtn');
    if (filterMaletinesBtn) {
        filterMaletinesBtn.addEventListener('click', filterMaletines);
    }
    
    const clearFiltersBtn = document.getElementById('clearFiltersBtn');
    if (clearFiltersBtn) {
        clearFiltersBtn.addEventListener('click', clearFilters);
    }
    
    // Formularios
    const maletinForm = document.getElementById('maletinForm');
    if (maletinForm) {
        maletinForm.addEventListener('submit', saveMaletin);
    }
    
    const discoForm = document.getElementById('discoForm');
    if (discoForm) {
        discoForm.addEventListener('submit', saveDisco);
    }
    
    const usuarioForm = document.getElementById('usuarioForm');
    if (usuarioForm) {
        usuarioForm.addEventListener('submit', saveUsuario);
    }
    
    // Botones de cancelar
    const cancelMaletinBtn = document.getElementById('cancelMaletinBtn');
    if (cancelMaletinBtn) {
        cancelMaletinBtn.addEventListener('click', () => closeMaletinModal());
    }
    
    const cancelDiscoBtn = document.getElementById('cancelDiscoBtn');
    if (cancelDiscoBtn) {
        cancelDiscoBtn.addEventListener('click', () => closeDiscoModal());
    }
    
    const cancelUsuarioBtn = document.getElementById('cancelUsuarioBtn');
    if (cancelUsuarioBtn) {
        cancelUsuarioBtn.addEventListener('click', () => closeUsuarioModal());
    }
}

// Configurar navegación
function setupNavigation() {
    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const section = link.getAttribute('data-section');
            showSection(section);
        });
    });
}

// Mostrar sección
function showSection(sectionName) {
    // Ocultar todas las secciones
    document.querySelectorAll('.content-section').forEach(section => {
        section.classList.remove('active');
    });
    
    // Remover clase active de todos los links
    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.remove('active');
    });
    
    // Mostrar sección seleccionada
    document.getElementById(sectionName).classList.add('active');
    
    // Activar link correspondiente
    document.querySelector(`[data-section="${sectionName}"]`).classList.add('active');
    
    currentSection = sectionName;
    
    // Cargar datos según la sección
    switch(sectionName) {
        case 'dashboard':
            loadDashboardData();
            break;
        case 'maletines':
            loadMaletines();
            break;
        case 'discos':
            loadDiscos();
            break;
        case 'usuarios':
            loadUsuarios();
            break;
    }
}

// Cargar datos del dashboard
function loadDashboardData() {
    Promise.all([
        loadMaletines(),
        loadDiscos(),
        loadUsuarios()
    ]).then(() => {
        updateDashboardStats();
        loadRecentActivity();
    }).catch(error => {
        console.error('Error cargando datos del dashboard:', error);
        showNotification('Error cargando datos del dashboard', 'error');
    });
}

function updateDashboardStats() {
    document.getElementById('totalMaletines').textContent = maletines.length;
    document.getElementById('totalDiscos').textContent = discos.length;
    document.getElementById('totalUsuarios').textContent = usuarios.length;
    
    const pendientes = maletines.filter(m => !m.fechaEntrega).length;
    document.getElementById('maletinesPendientes').textContent = pendientes;
}

function loadRecentActivity() {
    const activityContainer = document.getElementById('recentActivity');
    
    // Obtener los últimos 5 maletines creados
    const recentMaletines = maletines
        .sort((a, b) => new Date(b.fechaEnvio) - new Date(a.fechaEnvio))
        .slice(0, 5);
    
    if (recentMaletines.length === 0) {
        activityContainer.innerHTML = '<p class="no-data">No hay actividad reciente</p>';
        return;
    }
    
    const activityHTML = recentMaletines.map(maletin => `
        <div class="activity-item">
            <div class="activity-icon">💼</div>
            <div class="activity-content">
                <p><strong>Maletín #${maletin.id}</strong> enviado a <strong>${maletin.cliente}</strong></p>
                <small>${formatDateTime(maletin.fechaEnvio)}</small>
            </div>
            <div class="activity-status ${maletin.fechaEntrega ? 'delivered' : 'pending'}">
                ${maletin.fechaEntrega ? '✅ Entregado' : '⏳ Pendiente'}
            </div>
        </div>
    `).join('');
    
    activityContainer.innerHTML = activityHTML;
}

// Cargar maletines
function loadMaletines() {
    return fetch(`${API_BASE_URL}/maletines`, {
        method: 'GET',
        headers: getAuthHeaders()
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`Error ${response.status}: ${response.statusText}`);
        }
        return response.json();
    })
    .then(data => {
        maletines = Array.isArray(data) ? data : (data.data || []);
        if (currentSection === 'maletines') {
            renderMaletinesTable(maletines);
        }
        return maletines;
    })
    .catch(error => {
        console.error('Error cargando maletines:', error);
        maletines = [];
        if (currentSection === 'maletines') {
            document.querySelector('#maletinesTable tbody').innerHTML = 
                '<tr><td colspan="7" class="no-data">Error cargando datos</td></tr>';
        }
        throw error;
    });
}

// Renderizar tabla de maletines
function renderMaletinesTable(data = maletines) {
    const tbody = document.querySelector('#maletinesTable tbody');
    if (!tbody) return;
    
    if (!data || data.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" class="no-data">No hay maletines registrados</td></tr>';
        return;
    }
    
    tbody.innerHTML = data.map(maletin => {
        const discoInfo = maletin.disco ? `#${maletin.disco.id} (${maletin.disco.estado})` : 'N/A';
        const statusClass = maletin.fechaEntrega ? 'status-delivered' : 'status-pending';
        const statusText = maletin.fechaEntrega ? 'Entregado' : 'Pendiente';
        
        return `
            <tr>
                <td><strong>#${maletin.id}</strong></td>
                <td>${maletin.cliente || 'N/A'}</td>
                <td>${maletin.cajero || 'N/A'}</td>
                <td>${discoInfo}</td>
                <td>${formatDate(maletin.fechaEnvio)}</td>
                <td>
                    <span class="status ${statusClass}">${statusText}</span>
                    ${maletin.fechaEntrega ? `<br><small>${formatDate(maletin.fechaEntrega)}</small>` : ''}
                </td>
                <td class="actions">
                    <button class="btn btn-sm btn-primary" onclick="editMaletin(${maletin.id})" title="Editar">
                        ✏️
                    </button>
                    ${!maletin.fechaEntrega ? `
                        <button class="btn btn-sm btn-success" onclick="registrarEntrega(${maletin.id})" title="Marcar como entregado">
                            ✅
                        </button>
                    ` : ''}
                    <button class="btn btn-sm btn-danger" onclick="deleteMaletin(${maletin.id})" title="Eliminar">
                        🗑️
                    </button>
                </td>
            </tr>
        `;
    }).join('');
}

// Cargar discos
function loadDiscos() {
    return fetch(`${API_BASE_URL}/discos`, {
        method: 'GET',
        headers: getAuthHeaders()
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`Error ${response.status}: ${response.statusText}`);
        }
        return response.json();
    })
    .then(data => {
        discos = Array.isArray(data) ? data : (data.data || []);
        if (currentSection === 'discos') {
            renderDiscosTable(discos);
        }
        loadDiscosSelect(); // Para el modal de maletines
        return discos;
    })
    .catch(error => {
        console.error('Error cargando discos:', error);
        discos = [];
        if (currentSection === 'discos') {
            document.querySelector('#discosTable tbody').innerHTML = 
                '<tr><td colspan="4" class="no-data">Error cargando datos</td></tr>';
        }
        throw error;
    });
}

// Renderizar tabla de discos
function renderDiscosTable(data = discos) {
    const tbody = document.querySelector('#discosTable tbody');
    if (!tbody) return;
    
    if (!data || data.length === 0) {
        tbody.innerHTML = '<tr><td colspan="4" class="no-data">No hay discos registrados</td></tr>';
        return;
    }
    
    tbody.innerHTML = data.map(disco => {
        const estadoClass = disco.estado === 'DISPONIBLE' ? 'status-available' : 
                           disco.estado === 'EN_USO' ? 'status-in-use' : 'status-maintenance';
        
        return `
            <tr>
                <td><strong>#${disco.id}</strong></td>
                <td>
                    <span class="status ${estadoClass}">${disco.estado}</span>
                </td>
                <td>${disco.horasUso || 0} horas</td>
                <td class="actions">
                    <button class="btn btn-sm btn-primary" onclick="editDisco(${disco.id})" title="Editar">
                        ✏️
                    </button>
                    <button class="btn btn-sm btn-danger" onclick="deleteDisco(${disco.id})" title="Eliminar">
                        🗑️
                    </button>
                </td>
            </tr>
        `;
    }).join('');
}

// Cargar usuarios
function loadUsuarios() {
    return fetch(`${API_BASE_URL}/usuarios`, {
        method: 'GET',
        headers: getAuthHeaders()
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`Error ${response.status}: ${response.statusText}`);
        }
        return response.json();
    })
    .then(data => {
        usuarios = Array.isArray(data) ? data : (data.data || []);
        if (currentSection === 'usuarios') {
            renderUsuariosTable(usuarios);
        }
        return usuarios;
    })
    .catch(error => {
        console.error('Error cargando usuarios:', error);
        usuarios = [];
        if (currentSection === 'usuarios') {
            document.querySelector('#usuariosTable tbody').innerHTML = 
                '<tr><td colspan="7" class="no-data">Error cargando datos</td></tr>';
        }
        throw error;
    });
}

// Renderizar tabla de usuarios
function renderUsuariosTable(data = usuarios) {
    const tbody = document.querySelector('#usuariosTable tbody');
    if (!tbody) return;
    
    if (!data || data.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" class="no-data">No hay usuarios registrados</td></tr>';
        return;
    }
    
    tbody.innerHTML = data.map(usuario => {
        const isCurrentUser = currentUser && currentUser.id === usuario.id;
        
        return `
            <tr ${isCurrentUser ? 'class="current-user"' : ''}>
                <td><strong>#${usuario.id}</strong></td>
                <td>
                    ${usuario.username || 'N/A'}
                    ${isCurrentUser ? '<span class="badge badge-primary">Tú</span>' : ''}
                </td>
                <td>${usuario.email}</td>
                <td>${formatDateTime(usuario.createdAt)}</td>
                <td class="actions">
                    <button class="btn btn-sm btn-primary" onclick="editUsuario(${usuario.id})" title="Editar" ${isCurrentUser ? 'disabled' : ''}>
                        ✏️
                    </button>
                    <button class="btn btn-sm btn-danger" onclick="deleteUsuario(${usuario.id})" title="Eliminar" ${isCurrentUser ? 'disabled' : ''}>
                        🗑️
                    </button>
                </td>
            </tr>
        `;
    }).join('');
}

// Filtrar maletines
function filterMaletines() {
    const clienteFilter = document.getElementById('clienteFilter').value.toLowerCase();
    const cajeroFilter = document.getElementById('cajeroFilter').value.toLowerCase();
    
    const filtered = maletines.filter(maletin => {
        const matchCliente = !clienteFilter || maletin.cliente.toLowerCase().includes(clienteFilter);
        const matchCajero = !cajeroFilter || maletin.cajero.toLowerCase().includes(cajeroFilter);
        return matchCliente && matchCajero;
    });
    
    renderMaletinesTable(filtered);
}

// Limpiar filtros
function clearFilters() {
    document.getElementById('clienteFilter').value = '';
    document.getElementById('cajeroFilter').value = '';
    renderMaletinesTable(maletines);
}

// Configurar modales
function setupModals() {
    // Cerrar modales al hacer click en la X
    document.querySelectorAll('.close').forEach(closeBtn => {
        closeBtn.addEventListener('click', (e) => {
            const modal = e.target.closest('.modal');
            modal.style.display = 'none';
        });
    });
    
    // Cerrar modales al hacer click fuera
    window.addEventListener('click', (e) => {
        if (e.target.classList.contains('modal')) {
            e.target.style.display = 'none';
        }
    });
}

// Funciones de modal para Maletín
function openMaletinModal(maletin = null) {
    const modal = document.getElementById('maletinModal');
    const title = document.getElementById('maletinModalTitle');
    const form = document.getElementById('maletinForm');
    
    if (maletin) {
        title.textContent = 'Editar Maletín';
        document.getElementById('maletinId').value = maletin.id;
        document.getElementById('cliente').value = maletin.cliente;
        document.getElementById('cajero').value = maletin.cajero;
        document.getElementById('discoSelect').value = maletin.disco.id;
    } else {
        title.textContent = 'Agregar Maletín';
        form.reset();
        document.getElementById('maletinId').value = '';
    }
    
    loadDiscosSelect();
    modal.style.display = 'block';
}

function closeMaletinModal() {
    document.getElementById('maletinModal').style.display = 'none';
}

function loadDiscosSelect() {
    const select = document.getElementById('discoSelect');
    select.innerHTML = '<option value="">Seleccionar disco</option>';
    
    discos.forEach(disco => {
        const option = document.createElement('option');
        option.value = disco.id;
        option.textContent = `Disco #${disco.id} - ${disco.estado} (${disco.horasUso}h)`;
        select.appendChild(option);
    });
}

// Funciones de modal para Disco
function openDiscoModal(disco = null) {
    const modal = document.getElementById('discoModal');
    const title = document.getElementById('discoModalTitle');
    const form = document.getElementById('discoForm');
    
    if (disco) {
        title.textContent = 'Editar Disco';
        document.getElementById('discoId').value = disco.id;
        document.getElementById('estado').value = disco.estado;
        document.getElementById('horasUso').value = disco.horasUso;
    } else {
        title.textContent = 'Agregar Disco';
        form.reset();
        document.getElementById('discoId').value = '';
    }
    
    modal.style.display = 'block';
}

function closeDiscoModal() {
    document.getElementById('discoModal').style.display = 'none';
}

// Funciones de modal para Usuario
function openUsuarioModal(usuario = null) {
    const modal = document.getElementById('usuarioModal');
    const title = document.getElementById('usuarioModalTitle');
    const form = document.getElementById('usuarioForm');
    
    if (usuario) {
        title.textContent = 'Editar Usuario';
        document.getElementById('usuarioId').value = usuario.id;
        document.getElementById('emailUsuario').value = usuario.email;
        document.getElementById('nombreCompleto').value = usuario.nombreCompleto;
        document.getElementById('rolUsuario').value = usuario.rolUsuario;
        document.getElementById('activo').value = usuario.activo.toString();
    } else {
        title.textContent = 'Agregar Usuario';
        form.reset();
        document.getElementById('usuarioId').value = '';
    }
    
    modal.style.display = 'block';
}

function closeUsuarioModal() {
    document.getElementById('usuarioModal').style.display = 'none';
}

// Guardar maletín
function saveMaletin(e) {
    e.preventDefault();
    
    const id = document.getElementById('maletinId').value;
    const data = {
        cliente: document.getElementById('cliente').value,
        cajero: document.getElementById('cajero').value,
        disco: { id: parseInt(document.getElementById('discoSelect').value) }
    };
    
    const url = id ? `${API_BASE_URL}/maletines/${id}` : `${API_BASE_URL}/maletines`;
    const method = id ? 'PUT' : 'POST';
    
    fetch(url, {
        method: method,
        headers: {
            ...getAuthHeaders(),
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(() => {
        closeMaletinModal();
        loadMaletines();
        if (appState.currentSection === 'dashboard') {
            loadDashboardData();
        }
    })
    .catch(error => {
        console.error('Error guardando maletín:', error);
        alert('Error al guardar el maletín');
    });
}

// Guardar disco
function saveDisco(e) {
    e.preventDefault();
    
    const id = document.getElementById('discoId').value;
    const data = {
        estado: document.getElementById('estado').value,
        horasUso: parseInt(document.getElementById('horasUso').value)
    };
    
    const url = id ? `${API_BASE_URL}/discos/${id}` : `${API_BASE_URL}/discos`;
    const method = id ? 'PUT' : 'POST';
    
    fetch(url, {
        method: method,
        headers: {
            ...getAuthHeaders(),
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(() => {
        closeDiscoModal();
        loadDiscos();
        if (appState.currentSection === 'dashboard') {
            loadDashboardData();
        }
    })
    .catch(error => {
        console.error('Error guardando disco:', error);
        alert('Error al guardar el disco');
    });
}

// Guardar usuario
function saveUsuario(e) {
    e.preventDefault();
    
    const id = document.getElementById('usuarioId').value;
    const data = {
        email: document.getElementById('emailUsuario').value,
        nombreCompleto: document.getElementById('nombreCompleto').value,
        rolUsuario: document.getElementById('rolUsuario').value,
        activo: document.getElementById('activo').value === 'true'
    };
    
    const url = id ? `${API_BASE_URL}/usuarios/${id}` : `${API_BASE_URL}/usuarios`;
    const method = id ? 'PUT' : 'POST';
    
    fetch(url, {
        method: method,
        headers: {
            ...getAuthHeaders(),
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(() => {
        closeUsuarioModal();
        loadUsuarios();
        if (appState.currentSection === 'dashboard') {
            loadDashboardData();
        }
    })
    .catch(error => {
        console.error('Error guardando usuario:', error);
        alert('Error al guardar el usuario');
    });
}

// Editar funciones
function editMaletin(id) {
    const maletin = maletines.find(m => m.id === id);
    if (maletin) {
        openMaletinModal(maletin);
    }
}

function editDisco(id) {
    const disco = discos.find(d => d.id === id);
    if (disco) {
        openDiscoModal(disco);
    }
}

function editUsuario(id) {
    const usuario = usuarios.find(u => u.id === id);
    if (usuario) {
        openUsuarioModal(usuario);
    }
}

// Eliminar funciones
function deleteMaletin(id) {
    if (confirm('¿Estás seguro de que quieres eliminar este maletín?')) {
        fetch(`${API_BASE_URL}/maletines/${id}`, {
            method: 'DELETE',
            headers: getAuthHeaders()
        })
        .then(() => {
            loadMaletines();
            if (currentSection === 'dashboard') {
                loadDashboardData();
            }
        })
        .catch(error => {
            console.error('Error eliminando maletín:', error);
            alert('Error al eliminar el maletín');
        });
    }
}

function deleteDisco(id) {
    if (confirm('¿Estás seguro de que quieres eliminar este disco?')) {
        fetch(`${API_BASE_URL}/discos/${id}`, {
            method: 'DELETE',
            headers: getAuthHeaders()
        })
        .then(() => {
            loadDiscos();
            if (appState.currentSection === 'dashboard') {
                loadDashboardData();
            }
        })
        .catch(error => {
            console.error('Error eliminando disco:', error);
            alert('Error al eliminar el disco');
        });
    }
}

function deleteUsuario(id) {
    if (confirm('¿Estás seguro de que quieres eliminar este usuario?')) {
        fetch(`${API_BASE_URL}/usuarios/${id}`, {
            method: 'DELETE',
            headers: getAuthHeaders()
        })
        .then(() => {
            loadUsuarios();
            if (appState.currentSection === 'dashboard') {
                loadDashboardData();
            }
        })
        .catch(error => {
            console.error('Error eliminando usuario:', error);
            alert('Error al eliminar el usuario');
        });
    }
}

// Registrar entrega
function registrarEntrega(id) {
    if (confirm('¿Confirmar la entrega de este maletín?')) {
        fetch(`${API_BASE_URL}/maletines/${id}/registrar-entrega`, {
            method: 'PATCH',
            headers: getAuthHeaders()
        })
        .then(response => response.json())
        .then(() => {
            loadMaletines();
            if (appState.currentSection === 'dashboard') {
                loadDashboardData();
            }
        })
        .catch(error => {
            console.error('Error registrando entrega:', error);
            alert('Error al registrar la entrega');
        });
    }
}

// Logout
function logout() {
    if (confirm('¿Estás seguro de que deseas cerrar sesión?')) {
        // Limpiar localStorage inmediatamente
        localStorage.removeItem('authToken');
        localStorage.removeItem('userEmail');
        
        fetch(`${API_BASE_URL}/auth/logout`, {
            method: 'POST',
            headers: getAuthHeaders()
        })
        .then(() => {
            showNotification('Sesión cerrada exitosamente', 'success');
            setTimeout(() => {
                window.location.href = 'login.html';
            }, 1000);
        })
        .catch(error => {
            console.error('Error en logout:', error);
            showNotification('Error al cerrar sesión, redirigiendo...', 'error');
            setTimeout(() => {
                window.location.href = 'login.html';
            }, 1000);
        });
    }
}

// Utilidades
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('es-ES', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
    });
}