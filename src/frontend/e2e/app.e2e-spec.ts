import { RihaPage } from './app.po';

describe('riha App', () => {
  let page: RihaPage;

  beforeEach(() => {
    page = new RihaPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
