var catalogsTop4Section = document.getElementById("header-catalogs-top4-section");

fetch('/api/catalog/get-top-4')
    .then(response => response.json())
    .then(catalogs => {
        catalogs.forEach(catalog => {
            const tile = document.createElement('a');
            tile.className = 'header-catalogs-top4-tile';
            tile.href = '/catalog/'.concat(catalog.catalogUUID);

            const catalogPicture = document.createElement('img');
            catalogPicture.className = 'header-catalogs-top4-icon';
            catalogPicture.src = ''.concat(catalog.catalogPicture);

            const catalogName = document.createElement('span');
            catalogName.innerText = ''.concat(catalog.catalogName);

            tile.appendChild(catalogPicture);
            tile.appendChild(catalogName);
            catalogsTop4Section.appendChild(tile);
        });
    })
    .catch(error => {
        console.error('Error: ', error);
    });