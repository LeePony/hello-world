/*
* 2015.03.26
* Liu Huirong
* */
function slide(slide,slideImgLi,slideBtnUl,cur,speed,time){
    $(slideImgLi).each(function(index){
        $(slideBtnUl).append("<span></span>");
            $(this).hide();
        });
        $(slideBtnUl).find("span").first().addClass(cur);
        $(slideImgLi).first().show();

        $(slideBtnUl).find("span").each(function(index){
        $(this).click(function(){
            play(index);
            //clearInterval(scroll);
        });
    });

    var num=0;
    function move(){
        num=current();
        num=parseInt(num+1)==$(slideImgLi).length?0:num+1;
        play(num);
    }
    var scroll=setInterval(move,time);

    $(slide).mouseover(function(){
        clearInterval(scroll);
    });

    $(slide).mouseout(function(){
        scroll=setInterval(move,time);
    });

    function current() {
        $(slideBtnUl).find("span").each(function (index) {
            if ($(this).attr("class") == cur) {
                num = index;
            }
        });
        return num;
    }

    function play(index){
    $(slideImgLi).eq(index)
      .fadeIn(speed)
      .siblings()
      .fadeOut(speed);
    $(slideBtnUl).find("span").eq(index)
      .removeClass(cur)
      .addClass(cur)
      .siblings()
      .removeClass(cur);
    }

    $(slide).append("<a class='prev'>prev</a><a class='next'>next</a>");

    $(".prev").click(function(){
        num=current();
        play(num-1);
        //clearInterval(scroll);
    });
    $(".next").click(function(){
        num=current();
        num=parseInt(num+1)==$(slideImgLi).length?0:num+1;
        play(num);
        //clearInterval(scroll);
    });
    $(slideImgLi).on("tap",function(){
        move();
    });
    $(slideImgLi).on("swipeleft",function(){
        move();
    });
}