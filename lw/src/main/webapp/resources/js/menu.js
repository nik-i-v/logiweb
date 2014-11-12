function Menu(options) {
    var self = this;

    var elem = options.elem;

    // отмена выделения при клике на меню
    elem.on('mousedown selectstart', false);
    elem.on('click', '.menu-title', onTitleClick);

    // ---------- методы ----------

    function onTitleClick() {
        if (elem.hasClass('menu-open')) {
            self.close();
        } else {
            self.open();
        }
    }

    this.open = function() {
        elem.addClass('menu-open');
    };

    this.close = function() {
        elem.removeClass('menu-open');
    };

}