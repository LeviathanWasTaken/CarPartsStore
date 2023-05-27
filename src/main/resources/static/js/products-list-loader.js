const productTemplate = document.getElementById("product-template");
const productListElement = document.getElementById("products-list-container");
loadProductsList()
function loadProductsList() {
    var isUserAuthenticated;
    fetch('/api/user/api/user/is-logged-in')
        .then(response => response.json())
        .then(data => {
            if (data === true) {
                isUserAuthenticated = true;
            }
            else isUserAuthenticated = false;
        }).catch(error => console.error(error));
    productListElement.innerHTML = '';
    fetch(`/api/products/${catalogUUID}?sort=${sortingType}`)
        .then(response => response.json())
        .then(products => {
            document.getElementById("products-title").innerText = `Products list (${products.length} items found)`;
            products.forEach(product => {
                const productTileElement = document.createElement("div");
                productTileElement.innerHTML = productTemplate.innerHTML;

                productTileElement.querySelector(".product-tile").id = product.productUUID;
                productTileElement.querySelector(".product-img").src = product.previewPicture;
                productTileElement.querySelector(".product-name").href = `/product/${product.productUUID}`;
                productTileElement.querySelector(".product-name").innerText = product.productName;

                const productAttributesDiv = productTileElement.querySelector(".product-attributes");
                product.productAttributes.forEach(attribute => {
                    const productAttributeDiv = document.createElement("div");
                    productAttributeDiv.className = "product-attribute";
                    productAttributeDiv.innerHTML = `
      <img class="product-attribute-img" src="${attribute.attributePicture}" alt="">
      <p class="product-attribute-value">${attribute.attributeValue}</p>
      <span class="product-attribute-title">${attribute.attributeName}</span>
    `;
                    productAttributesDiv.appendChild(productAttributeDiv);
                });

                const productRatingLink = productTileElement.querySelector(".product-rating");
                productRatingLink.href = `/product/${product.productUUID}#reviews`;
                productRatingLink.innerText = `Rating: ${product.productRating}`;

                const productPriceValueSpans = productTileElement.querySelectorAll(".product-price-value");
                productPriceValueSpans.forEach(span => {
                    span.innerText = (product.productPriceInPennies/100).toFixed(2);
                });

                const cartButtonContainer = productTileElement.querySelector(".cart-button-section");
                const addToCartHandler = function () {
                    addToCart(product.productUUID, cartButtonContainer);
                };

                if (isUserAuthenticated) {
                    if (product.productInCart) {
                        loadCartButton("go", cartButtonContainer, goToCart);
                    } else {
                        loadCartButton("add", cartButtonContainer, addToCartHandler);
                    }
                } else {
                    loadCartButton("login", cartButtonContainer, login);
                }

                productListElement.appendChild(productTileElement);
            });
        }).catch(error => {
            console.error('Error ', error);
    });
}
function loadCartButton(type, cartButtonContainer, onClickHandler) {
    if (type === "go") {
        const cartButton = document.createElement("button");
        cartButton.addEventListener("click", onClickHandler);
        cartButton.innerText = "In cart";
        cartButton.className = "go-to-cart-button";
        cartButtonContainer.appendChild(cartButton);
    } else if (type === "add") {
        const cartButton = document.createElement("button");
        cartButton.addEventListener("click", onClickHandler);
        cartButton.innerText = "Add to cart";
        cartButton.className = "add-to-cart-button";
        cartButtonContainer.appendChild(cartButton);
    }
    else if (type === "login") {
        const cartButton = document.createElement("button");
        cartButton.addEventListener("click", onClickHandler);
        cartButton.innerText = "Log in to add items in cart";
        cartButton.className = "unauthenticated-add-to-cart-button";
        cartButtonContainer.appendChild(cartButton);
    }
    else console.error('Cant resolve type ', type);
}

function login() {
    window.location.href = "/login";
}
