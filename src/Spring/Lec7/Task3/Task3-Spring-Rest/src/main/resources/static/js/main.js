// Site modals: confirm for delete + info for alerts
document.addEventListener('DOMContentLoaded', function () {
    const confirmModal = document.getElementById('confirm-modal');
    const confirmMsg = document.getElementById('confirm-message');
    const btnYes = document.getElementById('confirm-yes');
    const btnNo = document.getElementById('confirm-no');
    const infoModal = document.getElementById('info-modal');
    const infoMsg = document.getElementById('info-message');
    const infoOk = document.getElementById('info-ok');

    let pendingForm = null;

    window.showInfo = function (message) {
        if (!infoModal) { alert(message); return; }
        infoMsg.textContent = message;
        infoModal.classList.remove('hidden');
    };
    if (infoOk) infoOk.addEventListener('click', () => infoModal.classList.add('hidden'));
    if (infoModal) {
        infoModal.addEventListener('click', e => { if (e.target === infoModal) infoModal.classList.add('hidden'); });
    }

    function openConfirm(message, form) {
        pendingForm = form;
        confirmMsg.textContent = message;
        confirmModal.classList.remove('hidden');
    }
    function closeConfirm() {
        confirmModal.classList.add('hidden');
        pendingForm = null;
    }

    // Intercept every .delete-form (single delete + delete-all + bulk delete hidden form)
    document.querySelectorAll('.delete-form').forEach(function (form) {
        form.addEventListener('submit', function (e) {
            e.preventDefault();
            const custom = form.getAttribute('data-confirm');
            const isDeleteAll = form.action.includes('/web/employees') && !form.action.includes('/delete/list') && form.querySelector('input[name=\"_method\"][value=\"DELETE\"]') && form.id !== 'bulk-delete-form';
            // bulk-delete-form is hidden and triggered via JS, handle separately in list-bulk.js
            let message = custom || 'Are you sure? This will call the REST API via RestTemplate.';
            if (custom) message = custom;
            else if (isDeleteAll) message = 'Are you sure you want to delete ALL employees? This will call DELETE /employees via RestTemplate.';
            else if (form.id === 'bulk-delete-form') message = 'Are you sure you want to delete selected employees?';
            else message = 'Are you sure you want to delete this employee? This will call DELETE /employees/{id} via RestTemplate.';
            openConfirm(message, form);
        });
    });

    // Also handle the bulk-delete form when submitted programmatically (still goes through above listener)
    btnYes.addEventListener('click', function () {
        if (pendingForm) {
            const form = pendingForm;
            closeConfirm();
            form.submit();
        }
    });
    btnNo.addEventListener('click', closeConfirm);
    confirmModal.addEventListener('click', function (e) {
        if (e.target === confirmModal) closeConfirm();
    });
    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape') {
            if (!confirmModal.classList.contains('hidden')) closeConfirm();
            if (infoModal && !infoModal.classList.contains('hidden')) infoModal.classList.add('hidden');
        }
    });
});
