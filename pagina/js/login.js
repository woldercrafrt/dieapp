// Configuración de la API
const API_BASE_URL = 'http://localhost:8080/api';

document.addEventListener('DOMContentLoaded', function() {
    const step1 = document.getElementById('step1');
    const step2 = document.getElementById('step2');
    const messageDiv = document.getElementById('message');
    const emailInput = document.getElementById('email');
    const codeInput = document.getElementById('code');
    const requestCodeBtn = document.getElementById('requestCode');
    const verifyCodeBtn = document.getElementById('verifyCode');
    const requestNewCodeBtn = document.getElementById('requestNewCode');
    
    // Verificar si ya está autenticado
    checkIfAlreadyAuthenticated();
    
    // Función para mostrar mensajes
    function showMessage(text, isSuccess) {
        messageDiv.textContent = text;
        messageDiv.className = isSuccess ? 'message success' : 'message error';
        messageDiv.classList.remove('hidden');
    }
    
    // Función para ocultar mensaje
    function hideMessage() {
        messageDiv.classList.add('hidden');
    }
    
    // Función para mostrar loading en botón
    function showButtonLoading(button, textElement) {
        button.disabled = true;
        textElement.innerHTML = '<span class="loading"></span>Procesando...';
    }
    
    // Función para ocultar loading en botón
    function hideButtonLoading(button, textElement, originalText) {
        button.disabled = false;
        textElement.textContent = originalText;
    }
    
    // Verificar si ya está autenticado
    function checkIfAlreadyAuthenticated() {
        const token = localStorage.getItem('authToken');
        
        if (token) {
            fetch(`${API_BASE_URL}/auth/current-user`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            })
            .then(response => {
                if (response.ok) {
                    window.location.href = 'index.html';
                }
            })
            .catch(() => {
                // Token inválido, eliminarlo
                localStorage.removeItem('authToken');
                localStorage.removeItem('userEmail');
            });
        }
    }
    
    // Solicitar código
    requestCodeBtn.addEventListener('click', function() {
        const email = emailInput.value.trim();
        
        if (!email) {
            showMessage('Por favor, ingresa tu correo electrónico', false);
            return;
        }
        
        if (!isValidEmail(email)) {
            showMessage('Por favor, ingresa un correo electrónico válido', false);
            return;
        }
        
        hideMessage();
        showButtonLoading(requestCodeBtn, document.getElementById('requestCodeText'));
        
        fetch(`${API_BASE_URL}/auth/request-code`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email })
        })
        .then(response => response.json())
        .then(data => {
            hideButtonLoading(requestCodeBtn, document.getElementById('requestCodeText'), 'Solicitar Código de Verificación');
            
            if (data.success) {
                goToStep2();
                showMessage(data.message, true);
                startCodeTimer();
            } else {
                showMessage(data.message || 'Error al solicitar el código', false);
            }
        })
        .catch(error => {
            hideButtonLoading(requestCodeBtn, document.getElementById('requestCodeText'), 'Solicitar Código de Verificación');
            showMessage('Error de conexión. Verifica tu conexión a internet.', false);
            console.error('Error:', error);
        });
    });
    
    // Verificar código
    verifyCodeBtn.addEventListener('click', function() {
        const email = emailInput.value.trim();
        const code = codeInput.value.trim();
        
        if (!code) {
            showMessage('Por favor, ingresa el código de verificación', false);
            return;
        }
        
        if (code.length !== 6) {
            showMessage('El código debe tener 6 dígitos', false);
            return;
        }
        
        hideMessage();
        showButtonLoading(verifyCodeBtn, document.getElementById('verifyCodeText'));
        
        fetch(`${API_BASE_URL}/auth/verify-code`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, code })
        })
        .then(response => response.json())
        .then(data => {
            hideButtonLoading(verifyCodeBtn, document.getElementById('verifyCodeText'), 'Verificar e Iniciar Sesión');
            
            if (data.success && data.token) {
                // Guardar el token JWT en localStorage
                localStorage.setItem('authToken', data.token);
                localStorage.setItem('userEmail', data.email);
                
                showMessage('¡Autenticación exitosa! Redirigiendo...', true);
                setTimeout(() => {
                    window.location.href = 'index.html';
                }, 1500);
            } else {
                showMessage(data.message || 'Código inválido o expirado', false);
            }
        })
        .catch(error => {
            hideButtonLoading(verifyCodeBtn, document.getElementById('verifyCodeText'), 'Verificar e Iniciar Sesión');
            showMessage('Error de conexión. Intenta nuevamente.', false);
            console.error('Error:', error);
        });
    });
    
    // Solicitar nuevo código
    requestNewCodeBtn.addEventListener('click', function() {
        goToStep1();
        hideMessage();
        codeInput.value = '';
    });
    
    // Enter key handlers
    emailInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            requestCodeBtn.click();
        }
    });
    
    codeInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            verifyCodeBtn.click();
        }
    });
    
    // Auto-format code input
    codeInput.addEventListener('input', function(e) {
        let value = e.target.value.replace(/\D/g, ''); // Solo números
        if (value.length > 6) {
            value = value.substring(0, 6);
        }
        e.target.value = value;
    });
});

// Funciones de navegación
function goToStep1() {
    document.getElementById('step1').classList.remove('hidden');
    document.getElementById('step2').classList.add('hidden');
    document.getElementById('step1Indicator').classList.add('active');
    document.getElementById('step1Indicator').classList.remove('completed');
    document.getElementById('step2Indicator').classList.remove('active');
}

function goToStep2() {
    document.getElementById('step1').classList.add('hidden');
    document.getElementById('step2').classList.remove('hidden');
    document.getElementById('step1Indicator').classList.remove('active');
    document.getElementById('step1Indicator').classList.add('completed');
    document.getElementById('step2Indicator').classList.add('active');
    
    // Focus en el input del código
    setTimeout(() => {
        document.getElementById('code').focus();
    }, 100);
}

// Timer para el código
let codeTimer;
function startCodeTimer() {
    let timeLeft = 300; // 5 minutos
    
    codeTimer = setInterval(() => {
        timeLeft--;
        
        if (timeLeft <= 0) {
            clearInterval(codeTimer);
            document.getElementById('message').textContent = 'El código ha expirado. Solicita uno nuevo.';
            document.getElementById('message').className = 'message error';
            document.getElementById('message').classList.remove('hidden');
        }
    }, 1000);
}

// Validar email
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}