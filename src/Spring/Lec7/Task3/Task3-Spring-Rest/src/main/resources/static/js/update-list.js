// update-list: remove row from bulk update + reindex so PUT /employees/list gets correct indices
document.addEventListener('DOMContentLoaded', function () {
    const container = document.getElementById('rows-container');
    if (!container) return;

    function reindex() {
        container.querySelectorAll('.list-row').forEach((row, i) => {
            // update hidden id + text inputs name attributes
            row.querySelectorAll('input').forEach(input => {
                if (input.name) {
                    input.name = input.name.replace(/employees\[\d+\]/, `employees[${i}]`);
                }
                if (input.id) {
                    input.id = input.id.replace(/employees\d+/, `employees${i}`);
                }
            });
            // also update error spans? Thymeleaf errors are server-side, no need client-side
        });
    }

    container.addEventListener('click', function (e) {
        if (e.target.classList.contains('remove-row-btn')) {
            const row = e.target.closest('.list-row');
            if (row) {
                row.remove();
                reindex();
                // if no rows left, show message? keep form but empty will trigger server global error
                if (container.querySelectorAll('.list-row').length === 0) {
                    const form = document.getElementById('update-list-form');
                    if (form) {
                        const empty = document.createElement('p');
                        empty.className = 'empty';
                        empty.textContent = 'No employees left. All rows removed - submit will show error or go back to list.';
                        container.appendChild(empty);
                    }
                }
            }
        }
    });
});
