$(document).ready(function () {
    $("img").each(function () {
        var src = $(this).attr("src");
        if (src == 'Information not avaiable') { // or anything else you want to remove...
            if ($(this).attr("class") == "music-cover") {
                $(this).attr("src", "./images/Music-squashed.png")
            }
            else if ($(this).attr("class") == "film-cover"){
                $(this).attr("src", "./images/Film-squashed.png")
                $(this).attr("width", "185")
                $(this).attr("height", "278")

            }
            else if ($(this).attr("class") == "game-cover"){
                $(this).attr("src", "./images/Game-squashed.png")
            }
            else $(this).attr("src", "./images/Book-squashed.png")
        }
    });
    $("a").each(function () {
        var href = $(this).attr("href");
        if (href == 'Information not avaiable') { // or anything else you want to remove...
            $(this).removeAttr("href");
        }
    });
});