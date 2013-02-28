function showNotify()
{
	document.querySelector("#ss").innerText = 'dd';
	alert('ccc');
}
function init() {
    clicker = document.querySelector('#xx');
    clicker.addEventListener('click', showNotify, false);
}    
document.addEventListener('DOMContentLoaded', init);