const bgImg = document.querySelector(".bg-fade-img");
const slides = document.getElementById("opacity-slider");
const sliderValSpan = document.getElementById("slider-val");
const opacityValueSpan = document.getElementById("opacity-value");
const resetBtn = document.getElementById("resetBtn");

if(bgImg && slides) {
  const initOpacity = parseFloat(slides.value);
  bgImg.style.opacity = initOpacity;
  if (sliderValSpan) sliderValSpan.innerText = initOpacity.toFixed(2);
  if(opacityValueSpan) opacityValueSpan.innerText = initOpacity.toFixed(2);

  const updateOpacity = (value) => {
    const newOpacity = parseFloat(value);
    bgImg.style.opacity = newOpacity;
    if (sliderValSpan) sliderValSpan.innerText = newOpacity.toFixed(2);
    if(opacityValueSpan) opacityValueSpan.innerText = newOpacity.toFixed(2);
  };

  slides.addEventListener('input', (e) => updateOpacity(e.target.value));

  if(resetBtn){
    resetBtn.addEventListener('click', () => {
      slides.value = '0.18';
      updateOpacity(0.18);
    });
  }

  const cardElem = document.querySelector('.card');
  if(cardElem){
    cardElem.addEventListener('dbclick', () => {   // 修正为 dblclick
      slides.value = '0.18';
      updateOpacity(0.18);
    });
  }
}
