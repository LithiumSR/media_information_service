$(document).ready(function () {
    $("img").each(function() {
        var src = $(this).attr("src");
        if(src == 'Information not avaiable') { // or anything else you want to remove...
            if ($(this).attr("class")=="music-cover"){
                $(this).attr("src","./images/no_image_230x230.png")
            }
            else $(this).attr("src","./images/no_image_173x260.png");
        }
    });
    $("a").each(function() {
        var href = $(this).attr("href");
        if(href == 'Information not avaiable') { // or anything else you want to remove...
            $(this).removeAttr("href");
        }
    });
});
