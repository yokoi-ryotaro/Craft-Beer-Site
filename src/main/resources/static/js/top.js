const swiper = new Swiper(".swiper", {
	slidesPerView: 'auto',
	grabCursor: true,
	centeredSlides: true,
	loop: true,
	loopAdditionalSlides: 1,
	pagination: {
	  el: '.swiper-pagination',
	  clickable: true,
	},
	navigation: {
 	nextEl: '.swiper-button-next',
 	prevEl: '.swiper-button-prev',
	},
	speed: 800,
	autoplay: {
		speed: 1000,
		delay: 3000,
		disableOnInteraction: false,
	},
});