$(document).ready(function () {
    $("img[src='']").remove();
    $("a[href='']").innerText="Information not avaiable";
    $("a[href='']").removeAttribute("href");
});
