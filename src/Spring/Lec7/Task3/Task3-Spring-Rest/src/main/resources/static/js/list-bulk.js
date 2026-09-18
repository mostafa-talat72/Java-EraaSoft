// list page: select multiple employees for bulk edit/delete - uses site modals from main.js
document.addEventListener('DOMContentLoaded', function () {
    const selectAll = document.getElementById('select-all');
    const btnEdit = document.getElementById('btn-edit-selected');
    const btnDelete = document.getElementById('btn-delete-selected');
    const bulkDeleteForm = document.getElementById('bulk-delete-form');
    const bulkDeleteIds = document.getElementById('bulk-delete-ids');

    if (selectAll) {
        selectAll.addEventListener('change', function () {
            document.querySelectorAll('.row-check').forEach(c => c.checked = selectAll.checked);
        });
    }

    function getSelectedIds() {
        return Array.from(document.querySelectorAll('.row-check:checked')).map(c => c.value);
    }

    if (btnEdit) {
        btnEdit.addEventListener('click', function () {
            const ids = getSelectedIds();
            if (ids.length === 0) { window.showInfo ? window.showInfo('Select at least one employee') : alert('Select at least one employee'); return; }
            window.location.href = '/web/employees/update-list?ids=' + ids.join(',');
        });
    }

    if (btnDelete) {
        btnDelete.addEventListener('click', function () {
            const ids = getSelectedIds();
            if (ids.length === 0) { window.showInfo ? window.showInfo('Select at least one employee') : alert('Select at least one employee'); return; }
            bulkDeleteIds.value = ids.join(',');
            // Trigger the hidden delete-form - main.js will show site confirm modal instead of browser confirm
            bulkDeleteForm.requestSubmit();
        });
    }
});
