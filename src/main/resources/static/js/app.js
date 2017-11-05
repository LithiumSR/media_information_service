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
      this.mouseX = e.pageX - this.$refs.carta.offsetLeft - this.width / 2;
      this.mouseY = e.pageY - this.$refs.carta.offsetTop - this.height / 2;
    },
    handleMouseEnter() {
      clearTimeout(this.mouseLeaveDelay);
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