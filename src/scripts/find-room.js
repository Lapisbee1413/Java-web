// Find Room Page JavaScript

// District data for cities
const districtData = {
    'ho-chi-minh': [
        'Quận 1', 'Quận 2', 'Quận 3', 'Quận 4', 'Quận 5', 'Quận 6', 'Quận 7', 'Quận 8', 'Quận 9', 'Quận 10',
        'Quận 11', 'Quận 12', 'Quận Bình Thạnh', 'Quận Gò Vấp', 'Quận Phú Nhuận', 'Quận Tân Bình', 
        'Quận Tân Phú', 'Quận Thủ Đức', 'Huyện Bình Chánh', 'Huyện Cần Giờ', 'Huyện Củ Chi', 'Huyện Hóc Môn', 'Huyện Nhà Bè'
    ],
    'ha-noi': [
        'Quận Ba Đình', 'Quận Hoàn Kiếm', 'Quận Hai Bà Trưng', 'Quận Đống Đa', 'Quận Tây Hồ', 'Quận Cầu Giấy',
        'Quận Thanh Xuân', 'Quận Hoàng Mai', 'Quận Long Biên', 'Quận Nam Từ Liêm', 'Quận Bắc Từ Liêm', 'Quận Hà Đông'
    ],
    'da-nang': [
        'Quận Hải Châu', 'Quận Thanh Khê', 'Quận Sơn Trà', 'Quận Ngũ Hành Sơn', 'Quận Liên Chiểu', 'Quận Cẩm Lệ'
    ],
    'can-tho': [
        'Quận Ninh Kiều', 'Quận Ô Môn', 'Quận Bình Thuỷ', 'Quận Cái Răng', 'Quận Thốt Nốt'
    ],
    'hai-phong': [
        'Quận Hồng Bàng', 'Quận Lê Chân', 'Quận Ngô Quyền', 'Quận Kiến An', 'Quận Hải An', 'Quận Đồ Sơn'
    ]
};

// Initialize page
document.addEventListener('DOMContentLoaded', function() {
    // Initialize AOS and Feather Icons
    AOS.init({
        duration: 600,
        easing: 'ease-in-out',
        once: true
    });
    feather.replace();

    // Setup event listeners
    setupEventListeners();
    
    // Set minimum date for move-in date to today
    setMinDate();
});

// Setup all event listeners
function setupEventListeners() {
    // City change listener
    const citySelect = document.getElementById('city');
    if (citySelect) {
        citySelect.addEventListener('change', updateDistricts);
    }

    // Form submission
    const form = document.getElementById('findRoomForm');
    if (form) {
        form.addEventListener('submit', handleFormSubmit);
    }

    // Real-time validation
    setupRealTimeValidation();

    // Phone number formatting
    const phoneInput = document.getElementById('phone');
    if (phoneInput) {
        phoneInput.addEventListener('input', formatPhoneNumber);
    }
}

// Set minimum date for move-in date
function setMinDate() {
    const moveInDateInput = document.getElementById('moveInDate');
    if (moveInDateInput) {
        const today = new Date().toISOString().split('T')[0];
        moveInDateInput.setAttribute('min', today);
    }
}

// Update districts based on selected city
function updateDistricts() {
    const citySelect = document.getElementById('city');
    const districtSelect = document.getElementById('district');
    
    if (!citySelect || !districtSelect) return;
    
    const selectedCity = citySelect.value;

    // Clear current options
    districtSelect.innerHTML = '<option value="">Chọn quận/huyện</option>';

    if (selectedCity && districtData[selectedCity]) {
        districtData[selectedCity].forEach(district => {
            const option = document.createElement('option');
            option.value = district.toLowerCase().replace(/\s+/g, '-');
            option.textContent = district;
            districtSelect.appendChild(option);
        });
    }
}

// Setup real-time validation
function setupRealTimeValidation() {
    const requiredFields = document.querySelectorAll('input[required], select[required], textarea[required]');
    
    requiredFields.forEach(field => {
        field.addEventListener('blur', validateField);
        field.addEventListener('input', clearFieldError);
    });
}

// Validate individual field
function validateField(event) {
    const field = event.target;
    const value = field.value.trim();
    
    // Remove existing error styling
    field.classList.remove('error');
    removeFieldError(field);
    
    // Check if field is required and empty
    if (field.hasAttribute('required') && !value) {
        showFieldError(field, 'Trường này là bắt buộc');
        return false;
    }
    
    // Specific validations
    if (field.type === 'email' && value) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(value)) {
            showFieldError(field, 'Email không hợp lệ');
            return false;
        }
    }
    
    if (field.type === 'tel' && value) {
        const phoneRegex = /^[0-9]{10,11}$/;
        if (!phoneRegex.test(value.replace(/\s/g, ''))) {
            showFieldError(field, 'Số điện thoại không hợp lệ');
            return false;
        }
    }
    
    if (field.id === 'age' && value) {
        const age = parseInt(value);
        if (age < 18 || age > 100) {
            showFieldError(field, 'Tuổi phải từ 18 đến 100');
            return false;
        }
    }
    
    return true;
}

// Show field error
function showFieldError(field, message) {
    field.classList.add('error');
    
    const errorDiv = document.createElement('div');
    errorDiv.className = 'form-error';
    errorDiv.innerHTML = `<i data-feather="alert-circle" class="w-4 h-4"></i>${message}`;
    
    field.parentNode.appendChild(errorDiv);
    feather.replace();
}

// Remove field error
function removeFieldError(field) {
    const errorDiv = field.parentNode.querySelector('.form-error');
    if (errorDiv) {
        errorDiv.remove();
    }
}

// Clear field error on input
function clearFieldError(event) {
    const field = event.target;
    field.classList.remove('error');
    removeFieldError(field);
}

// Format phone number
function formatPhoneNumber(event) {
    let value = event.target.value.replace(/\D/g, '');
    
    if (value.length > 0) {
        if (value.length <= 3) {
            value = value;
        } else if (value.length <= 6) {
            value = value.slice(0, 3) + ' ' + value.slice(3);
        } else if (value.length <= 10) {
            value = value.slice(0, 3) + ' ' + value.slice(3, 6) + ' ' + value.slice(6);
        } else {
            value = value.slice(0, 3) + ' ' + value.slice(3, 6) + ' ' + value.slice(6, 10);
        }
    }
    
    event.target.value = value;
}

// Validate entire form
function validateForm() {
    const requiredFields = document.querySelectorAll('input[required], select[required], textarea[required]');
    let isValid = true;
    
    requiredFields.forEach(field => {
        if (!validateField({ target: field })) {
            isValid = false;
        }
    });
    
    // Check if at least one contact method is selected
    const contactMethods = document.querySelectorAll('input[name="contactMethods"]:checked');
    if (contactMethods.length === 0) {
        showNotification('Vui lòng chọn ít nhất một cách thức liên hệ', 'error');
        isValid = false;
    }
    
    // Check terms agreement
    const agreeTerms = document.getElementById('agreeTerms');
    if (!agreeTerms.checked) {
        showNotification('Vui lòng đồng ý với điều khoản sử dụng', 'error');
        isValid = false;
    }
    
    return isValid;
}

// Handle form submission
function handleFormSubmit(e) {
    e.preventDefault();
    
    if (!validateForm()) {
        showNotification('Vui lòng kiểm tra lại thông tin đã nhập', 'error');
        return;
    }
    
    // Show loading
    showLoading();
    
    // Collect form data
    const formData = collectFormData();
    
    // Simulate API call
    setTimeout(() => {
        hideLoading();
        showSuccessModal();
        
        // Here you would typically send the data to your backend
        console.log('Find room form data:', formData);
    }, 2000);
}

// Collect form data
function collectFormData() {
    const form = document.getElementById('findRoomForm');
    const formData = new FormData(form);
    
    // Convert FormData to object
    const data = {};
    for (let [key, value] of formData.entries()) {
        if (data[key]) {
            // Handle multiple values (checkboxes)
            if (Array.isArray(data[key])) {
                data[key].push(value);
            } else {
                data[key] = [data[key], value];
            }
        } else {
            data[key] = value;
        }
    }
    
    // Add additional processing
    data.submittedAt = new Date().toISOString();
    data.userAgent = navigator.userAgent;
    
    return data;
}

// Show loading overlay
function showLoading() {
    const loadingOverlay = document.getElementById('loadingOverlay');
    if (loadingOverlay) {
        loadingOverlay.classList.remove('hidden');
    }
}

// Hide loading overlay
function hideLoading() {
    const loadingOverlay = document.getElementById('loadingOverlay');
    if (loadingOverlay) {
        loadingOverlay.classList.add('hidden');
    }
}

// Show success modal
function showSuccessModal() {
    const successModal = document.getElementById('successModal');
    if (successModal) {
        successModal.classList.remove('hidden');
    }
}

// Hide success modal
function hideSuccessModal() {
    const successModal = document.getElementById('successModal');
    if (successModal) {
        successModal.classList.add('hidden');
    }
}

// Show notification
function showNotification(message, type = 'info') {
    // Create notification element
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.innerHTML = `
        <div class="flex items-center">
            <i data-feather="${getNotificationIcon(type)}" class="w-5 h-5 mr-2"></i>
            <span>${message}</span>
        </div>
    `;
    
    // Add to page
    document.body.appendChild(notification);
    feather.replace();
    
    // Show notification
    setTimeout(() => {
        notification.classList.add('show');
    }, 100);
    
    // Hide notification after 5 seconds
    setTimeout(() => {
        notification.classList.remove('show');
        setTimeout(() => {
            notification.remove();
        }, 300);
    }, 5000);
}

// Get notification icon based on type
function getNotificationIcon(type) {
    const icons = {
        'success': 'check-circle',
        'error': 'alert-circle',
        'warning': 'alert-triangle',
        'info': 'info'
    };
    return icons[type] || 'info';
}

// Utility functions
function formatPrice(price) {
    return new Intl.NumberFormat('vi-VN').format(price) + ' VNĐ';
}

function getCityName(cityCode) {
    const cities = {
        'ho-chi-minh': 'TP. Hồ Chí Minh',
        'ha-noi': 'Hà Nội',
        'da-nang': 'Đà Nẵng',
        'can-tho': 'Cần Thơ',
        'hai-phong': 'Hải Phòng',
        'other': 'Khác'
    };
    return cities[cityCode] || cityCode;
}

function getDistrictName(districtCode) {
    return districtCode.split('-').map(word => 
        word.charAt(0).toUpperCase() + word.slice(1)
    ).join(' ');
}

// Auto-save form data to localStorage
function autoSaveForm() {
    const form = document.getElementById('findRoomForm');
    if (!form) return;
    
    const formData = new FormData(form);
    const data = {};
    
    for (let [key, value] of formData.entries()) {
        data[key] = value;
    }
    
    localStorage.setItem('findRoomFormData', JSON.stringify(data));
}

// Load saved form data
function loadSavedFormData() {
    const savedData = localStorage.getItem('findRoomFormData');
    if (!savedData) return;
    
    try {
        const data = JSON.parse(savedData);
        const form = document.getElementById('findRoomForm');
        
        Object.keys(data).forEach(key => {
            const field = form.querySelector(`[name="${key}"]`);
            if (field) {
                if (field.type === 'checkbox' || field.type === 'radio') {
                    field.checked = data[key] === field.value;
                } else {
                    field.value = data[key];
                }
            }
        });
        
        // Update districts if city is selected
        const citySelect = document.getElementById('city');
        if (citySelect && citySelect.value) {
            updateDistricts();
            const districtSelect = document.getElementById('district');
            if (districtSelect && data.district) {
                districtSelect.value = data.district;
            }
        }
    } catch (error) {
        console.error('Error loading saved form data:', error);
    }
}

// Clear saved form data
function clearSavedFormData() {
    localStorage.removeItem('findRoomFormData');
}

// Setup auto-save
function setupAutoSave() {
    const form = document.getElementById('findRoomForm');
    if (!form) return;
    
    // Save on input change
    form.addEventListener('input', debounce(autoSaveForm, 1000));
    form.addEventListener('change', debounce(autoSaveForm, 1000));
    
    // Load saved data on page load
    loadSavedFormData();
}

// Debounce function
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// Initialize auto-save when page loads
document.addEventListener('DOMContentLoaded', function() {
    setupAutoSave();
});

// Clear saved data on successful submission
function handleFormSubmit(e) {
    e.preventDefault();
    
    if (!validateForm()) {
        showNotification('Vui lòng kiểm tra lại thông tin đã nhập', 'error');
        return;
    }
    
    // Show loading
    showLoading();
    
    // Collect form data
    const formData = collectFormData();
    
    // Simulate API call
    setTimeout(() => {
        hideLoading();
        showSuccessModal();
        clearSavedFormData(); // Clear saved data on success
        
        // Here you would typically send the data to your backend
        console.log('Find room form data:', formData);
    }, 2000);
}
