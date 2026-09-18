// add-list: add/remove dynamic rows for bulk add + reindex names
document.addEventListener('DOMContentLoaded', function () {
    const container = document.getElementById('rows-container');
    const addBtn = document.getElementById('add-row-btn');
    const template = document.getElementById('row-template');
    if (!container || !addBtn || !template) return;

    function reindex() {
        container.querySelectorAll('.list-row').forEach((row, i) => {
            const h3 = row.querySelector('h3');
            if (h3) h3.textContent = 'Employee #' + (i + 1);
            row.querySelectorAll('input').forEach(input => {
                if (input.name) {
                    input.name = input.name.replace(/employees\[\d+\]/, `employees[${i}]`);
                }
                if (input.id) {
                    // thymeleaf generates id like employees0.name -> update too
                    input.id = input.id.replace(/employees\d+/, `employees${i}`);
                }
            });
        });
    }

    addBtn.addEventListener('click', function () {
        const idx = container.querySelectorAll('.list-row').length;
        const html = template.innerHTML
            .replace(/__IDX__/g, idx)
            .replace(/__NUM__/g, idx + 1);
        container.insertAdjacentHTML('beforeend', html);
    });

    container.addEventListener('click', function (e) {
        if (e.target.classList.contains('remove-row-btn')) {
            const row = e.target.closest('.list-row');
            if (row) {
                row.remove();
                reindex();
                // keep at least one row visible: if all removed, add one empty
                if (container.querySelectorAll('.list-row').length === 0) {
                    addBtn.click();
                }
            }
        }
    });
});
