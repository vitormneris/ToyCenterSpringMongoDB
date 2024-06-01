document.addEventListener('DOMContentLoaded', function() {
    const inputQuantidade = document.getElementById('quantidade');
    const btnMenos = document.querySelector('.menos');
    const btnMais = document.querySelector('.mais');

    btnMenos.addEventListener('click', function() {
        let currentValue = parseInt(inputQuantidade.value);
        if (currentValue > 1) {
            inputQuantidade.value = currentValue - 1;
        }
    });

    btnMais.addEventListener('click', function() {
        let currentValue = parseInt(inputQuantidade.value);
        inputQuantidade.value = currentValue + 1;
    });
});

document.addEventListener('DOMContentLoaded', function () {
    const btnAdicionarCarrinho = document.querySelector('.buy2');
    const popupOverlay = document.querySelector('.popup-overlay');
    const btnFinalizar = document.getElementById('finalizarCompra');
    const btnContinuar = document.getElementById('ContinuarComprando');

    console.log('Script carregado e executado.');

    btnAdicionarCarrinho.addEventListener('click', function (event) {
        event.preventDefault();
        console.log('Botão "Adicionar ao Carrinho" clicado.');
        popupOverlay.style.display = 'flex';
    });

    btnFinalizar.addEventListener('click', function () {
        console.log('Botão "Finalizar Compra" clicado.');
        window.location.href = 'addCarrinho.html';
    });

    btnContinuar.addEventListener('click', function () {
        console.log('Botão "Continuar Comprando" clicado.');
        window.location.href = 'getAllToy.html';
    });
});
