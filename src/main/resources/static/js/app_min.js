Vue.component("carta", {
  template: `
    <div class="carta-wrap"
      @mousemove="handleMouseMove"
      @mouseenter="handleMouseEnter"
      @mouseleave="handleMouseLeave"
      ref="carta">
      <div class="carta"
        :style="cartaStyle">
        <div class="carta-bg" :style="[cartaBgTransform, cartaBgImage]"></div>
        <div class="carta-info">
          <slot name="header"></slot>
          <slot name="content"></slot>
        </div>
      </div>
    </div>`,
  mounted() {
    this.width = this.$refs.carta.offsetWidth;
    this.height = this.$refs.carta.offsetHeight;
  },
  props: ["dataImage"],
  data: () => ({
    width: 0,
    height: 0,
    mouseX: 0,
    mouseY: 0,
    mouseLeaveDelay: null
  }),
  computed: {
    mousePX() {
      return this.mouseX / this.width;
    },
    mousePY() {
      return this.mouseY / this.height;
    },
    cartaStyle() {
      const rX = this.mousePX * 30;
      const rY = this.mousePY * -30;
      return {
        transform: `rotateY(${rX}deg) rotateX(${rY}deg)`
      };
    },
    cartaBgTransform() {
      const tX = this.mousePX * -50;
      const tY = this.mousePY * -50;
      return {
        transform: `translateX(${tX}px) translateY(${tY}px)`
      };
    },
    cartaBgImage() {
      return {
        backgroundImage: `url(${this.dataImage})`
      };
    }
  },
  methods: {
    handleMouseMove(e) {

    },
    handleMouseEnter() {

    },
    handleMouseLeave() {
      this.mouseLeaveDelay = setTimeout(() => {
        this.mouseX = 0;
        this.mouseY = 0;
      }, 1000);
    }
  }
});

window.onload = function () {
  const graphic1 = new Vue({
    el: "#app"
  });
  const graphic2 = new Vue({
    el: "#social"
  })
}

window.addEventListener("beforeunload", function () {
  document.body.classList.add("animate-out");
});

function openChatWindow() {
  //var stile = "top=0, left=0, width=450, height=300, status=no, menubar=no, toolbar=no, resizable=no";
  //window.open("insert url here","",stile);
  window.location.href = "/webchat";
}

window.addEventListener("load", function () {
  window.cookieconsent.initialise({
    "palette": {
      "popup": {
        "background": "#252e39"
      },
      "button": {
        "background": "#4984E8"
      }
    },
    "theme": "classic",
    "position": "bottom-right"
  })
});