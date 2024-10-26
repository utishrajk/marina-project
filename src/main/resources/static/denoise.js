document.getElementById('fileInput').addEventListener('change', function(event) {
    const file = event.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            document.getElementById('selectedImage').src = e.target.result;
            document.getElementById('selectedImage').style.display = 'block';
        };
        reader.readAsDataURL(file);
    }
});

document.getElementById('denoiseForm').addEventListener('submit', function(event) {
    event.preventDefault();
    const formData = new FormData();
    const fileInput = document.getElementById('fileInput');
    formData.append('image', fileInput.files[0]);

    fetch('/denoise', {
        method: 'POST',
        body: formData
    })
    .then(response => response.blob())
    .then(blob => {
        const url = URL.createObjectURL(blob);
        const denoisedImage = document.getElementById('denoisedImage');
        denoisedImage.src = url;
        denoisedImage.style.display = 'block';
    })
    .catch(error => console.error('Error:', error));
});