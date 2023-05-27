loadUserBar();

/*
function loadUserBar() {
    var userBar = document.getElementById("user-bar");
    userBar.innerHTML = '';

    fetch('/api/user')
        .then(response => response.json())
        .then(user => {
            if (user.userUUID !== null) {
                const userInfo = createDivElement('user-info');
                const userCard = createDivElement('user-card');
                const userAvatar = createImageElement('user-avatar', user.avatar);
                const userDetails = createDivElement('user-details');
                const username = createDivElement('username', user.name);
                const userLinks = createDivElement('user-links');
                const logoutLink = createAnchorElement('logout-btn', '/logout', 'Logout');
                const profileLink = createAnchorElement('edit-profile-btn', '/profile', 'Profile');
                const cartLink = createAnchorElement('cart', '/cart', null);
                const cartDetails = createDivElement('cart-details');
                const amountOfItemsInCart = createDivElement('amount-of-items-in-cart', user.totalQuantityOfItemsInCart + ' items');
                const totalPriceOfItemsInCart = createDivElement('total-price-of-items-in-cart', '$' + (parseInt(user.totalPriceOfItemsInCartInPennies) / 100).toFixed(2));
                const basket = createImageElement('', '/static/img/basket.svg');

                appendElements(cartDetails, amountOfItemsInCart, totalPriceOfItemsInCart);
                appendElements(cartLink, cartDetails, basket);
                appendElements(userLinks, profileLink, logoutLink);
                appendElements(userDetails, username, userLinks);
                appendElements(userCard, userAvatar, userDetails);
                appendElements(userInfo, userCard, cartLink);
                appendElement(userBar, userInfo);
            } else {
                const loginLink = createAnchorElement('login-link', '/login', 'Login with Github');
                appendElement(userBar, loginLink);
            }
        }).catch(error => {
        console.error('Error: ', error);
    });
}

 */
function loadUserBar() {
    const userBar = document.getElementById("user-bar");
    userBar.innerHTML = '';

    fetch('/api/user')
        .then(response => response.json())
        .then(user => {
            if (user.userUUID !== null) {
                const template = `
          <div class="user-info">
            <div class="user-card">
              <img class="user-avatar" src="${user.avatar}">
              <div class="user-details">
                <div class="username">${user.name}</div>
                <div class="user-links">
                  <a class="edit-profile-btn" href="/profile">Profile</a>
                  <a class="logout-btn" href="/logout">Logout</a>
                </div>
              </div>
            </div>
            <a class="cart" href="/cart">
              <div class="cart-details">
                <div class="amount-of-items-in-cart">${user.totalQuantityOfItemsInCart} items</div>
                <div class="total-price-of-items-in-cart">$${(user.totalPriceOfItemsInCartInPennies / 100).toFixed(2)}</div>
              </div>
              <img src="/static/img/basket.svg">
            </a>
          </div>
        `;
                userBar.innerHTML = template;
            } else {
                const loginLink = createAnchorElement('login-link', '/login', 'Login with Github');
                appendElement(userBar, loginLink);
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

