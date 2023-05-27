var catalogList = document.getElementById("home-catalog-list");

fetch('/api/catalog/get-top-4')
    .then(response => response.json())
    .then(catalogs => {
        catalogs.forEach(catalog => {
            const tile = createDivElement('catalog-tile');
            const catalogName = createAnchorElement('catalog-name', '/catalog/'.concat(catalog.catalogUUID), null);
            const catalogImg = createImageElement('catalog-tile-img', catalog.catalogPicture);
            const catalogP = createParagraphElement('catalog-name-p', catalog.catalogName);

            appendElements(catalogName, catalogImg, catalogP);
            appendElements(tile, catalogName);
            appendElements(catalogList, tile);
        });
    })
    .catch(error => {
        console.error('Error: ', error);
    });