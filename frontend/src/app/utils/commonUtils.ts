export const makeRecaptchaBadgeVisible = (): HTMLStyleElement => {
  const css = '.grecaptcha-badge { visibility: visible !important;}';
  const head = document.getElementsByTagName('head')[0];
  const style = document.createElement('style');
  style.type = 'text/css';
  style.appendChild(document.createTextNode(css));
  head.appendChild(style);
  return style;
}

export const makeRecaptchaBadgeInvisible = (recaptchaBadgeStyle: HTMLStyleElement): void => {
  document.getElementsByTagName('head')[0].removeChild(recaptchaBadgeStyle);
}
