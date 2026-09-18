// Site modal instead of browser confirm() for all .delete-form
document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('confirm-modal');
    const msgEl = document.getElementById('confirm-message');
    const btnYes = document.getElementById('confirm-yes');
    const btnNo = document.getElementById('confirm-no');
    let pendingForm = null;

    function openModal(message, form) {
        pendingForm = form;
        msgEl.textContent = message;
        modal.classList.remove('hidden');
    }
    function closeModal() {
        modal.classList.add('hidden');
        pendingForm = null;
    }

    // All delete forms (single row) use site modal
    document.querySelectorAll('.delete-form').forEach(function (form) {
        form.addEventListener('submit', function (e) {
            e.preventDefault();
            openModal('Are you sure you want to delete this player? This will call DELETE via RestTemplate.', form);
        });
    });

    btnYes.addEventListener('click', function () {
        if (pendingForm) {
            const form = pendingForm;
            closeModal();
            // native submit bypasses the submit-listener (no loop)
            form.submit();
        }
    });
    btnNo.addEventListener('click', closeModal);
    modal.addEventListener('click', function (e) {
        if (e.target === modal) closeModal();
    });
    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape' && !modal.classList.contains('hidden')) closeModal();
    });
});
