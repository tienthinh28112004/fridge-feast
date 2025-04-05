document.addEventListener('DOMContentLoaded', function() {
            const itemsContainer = document.getElementById('itemsContainer');
            const addButton = document.getElementById('addButton');
            const createRecipeButton = document.getElementById('createRecipeButton');
            const placeholderText = document.getElementById('placeholderText');

            function updatePlaceholder() {
                placeholderText.style.display = itemsContainer.children.length > 0 ? 'none' : 'block';
            }

            function addNewItem() {
                const item = document.createElement('div');
                item.className = 'item';

                const input = document.createElement('input');
                input.type = 'text';
                input.placeholder = 'Nhập nguyên liệu...';

                const deleteBtn = document.createElement('button');
                deleteBtn.className = 'delete-btn';
                deleteBtn.innerHTML = '×';
                deleteBtn.addEventListener('click', function() {
                    item.remove();
                    updatePlaceholder();
                });

                item.appendChild(input);
                item.appendChild(deleteBtn);
                itemsContainer.appendChild(item);
                input.focus();
                updatePlaceholder();
            }

            addButton.addEventListener('click', addNewItem);

            createRecipeButton.addEventListener('click', function() {
                const ingredients = [...itemsContainer.querySelectorAll('.item input')]
                    .map(input => input.value.trim())
                    .filter(value => value !== '');

                if (ingredients.length === 0) {
                    alert('Vui lòng nhập ít nhất một nguyên liệu!');
                    return;
                }

                alert('Tạo món ăn với các nguyên liệu: ' + ingredients.join(', '));
            });

            document.addEventListener('keydown', function(event) {
                if (event.key === 'Enter' && document.activeElement.tagName === 'INPUT') {
                    addNewItem();
                }
            });

            addNewItem();
        });