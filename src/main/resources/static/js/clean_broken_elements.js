$(document).ready(function () {
    $("img").each(function() {
        var src = $(this).attr("src");
        if(src == 'Information not avaiable') { // or anything else you want to remove...
            $(this).remove();
        }
    });
    $("a").each(function() {
        var href = $(this).attr("href");
        if(href == 'Information not avaiable') { // or anything else you want to remove...
            $(this).removeAttr("href");
        }
    });
});
