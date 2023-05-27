function createDivElement(className, innerText) {
    const element = document.createElement('div');
    element.className = className;
    if (innerText) {
        element.innerText = innerText;
    }
    return element;
}

function createAnchorElement(className, href, innerText) {
    const element = document.createElement('a');
    element.className = className;
    element.href = href;
    element.innerText = innerText;
    return element;
}

function createParagraphElement(className, innerText) {
    const element = document.createElement('p');
    element.className = className;
    element.innerText = innerText;
    return element;
}

function createImageElement(className, src) {
    const element = document.createElement('img');
    element.className = className;
    element.src = src;
    return element;
}

function appendElements(parent, ...children) {
    children.forEach(child => parent.appendChild(child));
}

function appendElement(parent, child) {
    parent.appendChild(child);
}

