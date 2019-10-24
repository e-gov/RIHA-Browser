// Responsive
var windowW = $(window).width();
function responsive() {
    if (windowW < 576) {
        $('#langbar').prependTo('#mainmenu');
    } else {
        $('#langbar').appendTo('#header .header-top .container .left');
    }
    if ($('.header-alert:visible').length) {
        if(windowW < 1200){
            $('body').css('padding-top', '');
        } else{

        var alertH = $('.header-alert').outerHeight();
        var headerH = $('#header').outerHeight();
        $('body').css('padding-top', alertH);
        }
    }
};
$(document).ready(function () {
    responsive();
});
$(window).resize(function () {
    windowW = $(window).width();
    responsive();
});
/* Header alert */
$('.header-alert .alert').on('closed.bs.alert', function () {
    responsive();
})
/* Select 2*/
var query = {};
var $element = $('select.form-control');

function markMatch (text, term) {
  // Find where the match is
  var match = text.toUpperCase().indexOf(term.toUpperCase());

  var $result = $('<span></span>');

  // If there is no match, move on
  if (match < 0) {
    return $result.text(text);
  }

  // Put in whatever text is before the match
  $result.text(text.substring(0, match));

  // Mark the match
  var $match = $('<span class="select2-rendered__match"></span>');
  $match.text(text.substring(match, match + term.length));

  // Append the matching text
  $result.append($match);

  // Put in whatever is after the match
  $result.append(text.substring(match + term.length));

  return $result;
}

$element.select2({
    minimumResultsForSearch: 10,
  templateResult: function (item) {
    // No need to template the searching text
    if (item.loading) {
      return item.text;
    }

    var term = query.term || '';
    var $result = markMatch(item.text, term);

    return $result;
  },
  language: {
    searching: function (params) {
      // Intercept the query as it is happening
      query = params;

      // Change this to be appropriate for your application
      return 'Otsin…';
    }
  }
});

/* Bootstrap */
/* Accordion */
$('.accordion > .card .card-header a').click(function () {
    console.log($(this).attr('aria-expanded'));
    if ($(this).attr('aria-expanded') == 'true') {
        $(this).closest('.card').removeClass('open');
    } else {
        $(this).closest('.accordion').find('> .card').removeClass('open');
        $(this).closest('.card').addClass('open');
    }
});
/* Tooltip */
$('[data-toggle="tooltip"]').tooltip()
    /* Popover */
$('[data-toggle="popover"]').popover({
        template: '<div class="popover" role="tooltip"><button class="btn popover-close" onclick="jQuery(this).closest(&quot;.popover&quot;).popover(&quot;hide&quot;);"><span class="fa fa-times"></span></button><div class="popover-arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div></div>'
    })
    /* Autocomplete */
var availableTags = [
      "Tallinn",
        "Tartu",
        "Narva",
        "Pärnu",
        "Viljandi",
        "Võru",
        "Haapsalu",
        "Kohtla-Järve"
    ];
$('.autocomplete .form-control').each(function () {
    var container = $(this).closest('.autocomplete');
    $(this).val('');
    $(this).autocomplete({
        source: availableTags,
        appendTo: container,
        open: function (event, ui) {
            container.addClass('open');
        },
        close: function (event, ui)  {
            container.removeClass('open');
        }
    });
});
$.ui.autocomplete.prototype._renderItem = function (ul, item) {
    var t = String(item.value).replace(
        new RegExp(this.term, "gi"),
        "<em>$&</em>");
    return $("<li></li>")
        .data("item.autocomplete", item)
        .append("<div>" + t + "</div>")
        .appendTo(ul);
};
function checkAutocompleteVal(thisObj) {
    var formGroup = thisObj.closest('.form-group');
    var inputGroup = thisObj.closest('.input-group');
    var inputVal = thisObj.val().replace(/\s+/g, '');
    if (inputVal === '') {
        inputGroup.find('.input-group-overlay').fadeOut();
        if (inputGroup.hasClass('validation')) {
            formGroup.removeClass('has-danger');
            inputGroup.next('.alert').remove();
        }
    } else {
        inputGroup.find('.input-group-overlay').fadeIn();
    }
}
$('.autocomplete .form-control').each(function () {
    checkAutocompleteVal($(this));
});
$('.autocomplete .form-control').on('change keydown keyup', function () {
    checkAutocompleteVal($(this));
});
$('.autocomplete .reset').on('click', function () {
    $(this).closest('.input-group').find('input.form-control').val('');
    checkAutocompleteVal($(this));
    $(this).closest('.input-group').find('input.form-control').focus();
    $(this).closest('.input-group-overlay').fadeOut();
});
/* Autocomplete with validation */
$('.autocomplete.validation .form-control').each(function () {
    var container = $(this).closest('.autocomplete');
    var formGroup = $(this).closest('.form-group');
    $(this).val('');
    $(this).autocomplete({
        source: availableTags,
        appendTo: container,
        open: function (event, ui) {
            container.addClass('open');
        },
        close: function (event, ui)  {
            container.removeClass('open');
        },
        response: function (event, ui) {
            if (ui.content.length === 0) {
                formGroup.addClass('has-danger');
                if (container.next('.alert').length <= 0) {
                    container.after('<div class="alert form-alert alert-danger alert-dismissible" role="alert"><strong>Kas oled kindel, et sisestasid õige väärtuse?</strong><div class="form-links"><a href="#" data-dismiss="alert">Jah</a><a href="#">Ei</a></div></div>');
                }
            } else {
                formGroup.removeClass('has-danger');
                container.next('.alert').remove();
            }
        }
    });
});
/* Datepicker */
/* Check browser support for type="date" */
if ($('[type="date"]').prop('type') != 'date') {
    $(".datepicker .form-control").datepicker({
        changeMonth: true,
        changeYear: true,
        dateFormat: "dd.mm.yy",
        dayNamesMin: ['P', 'E', 'T', 'K', 'N', 'R', 'L'],
        monthNamesShort: ["Jaanuar", "Veebruar", "Märts", "Aprill", "Mai", "Juuni", "Juuli", "August", "September", "Oktoober", "November", "Detsember"],
        firstDay: 1,
        beforeShow: function (input, inst) {
            var widget = $(inst).datepicker('widget');
            widget.css('margin-left', $(input).closest('.input-group').outerWidth() - widget.outerWidth()).css('margin-top', '10px');
        }
    }).on('focusin', function () {
        console.log('open1');
        $(this).closest('.datepicker').addClass('open');
    }).on('focusout', function () {
        console.log('rem1');
        $(this).closest('.datepicker').removeClass('open');
    });
    $('.datepicker .open-datepicker').on('click tap', function (e) {
        e.preventDefault();
        var inputGroup = $(this).closest('.datepicker');
        if ($('#ui-datepicker-div').is(':visible')) {
            inputGroup.find('.form-control').focusout();
        } else {
            inputGroup.find('.form-control').focus().click();
        }
    });
    $('.datepicker').next('.form-links').find('.today').on('click', function (e) {
        e.preventDefault();
        var thisDatepicker = $(this).closest('.form-links').prev('.datepicker').find('.form-control');
        thisDatepicker.datepicker('setDate', new Date());
    });
    $('.datepicker').next('.form-links').find('.tomorrow').on('click', function (e) {
        e.preventDefault();
        var thisDatepicker = $(this).closest('.form-links').prev('.datepicker').find('.form-control');
        var tmrw = new Date((new Date()).valueOf() + 1000 * 3600 * 24);
        thisDatepicker.datepicker('setDate', tmrw);
    });
} else {
    $('.datepicker .open-datepicker').on('click tap', function (e) {
        e.preventDefault();
        var inputGroup = $(this).closest('.datepicker');
        inputGroup.find('.form-control').focus().click();
    });
    $('.datepicker').next('.form-links').find('.today').on('click', function (e) {
        e.preventDefault();
        var thisInput = $(this).closest('.form-links').prev('.datepicker').find('.form-control');
        var today = new Date().toISOString().substring(0, 10)
        thisInput.val(today);
    });
    $('.datepicker').next('.form-links').find('.tomorrow').on('click', function (e) {
        e.preventDefault();
        var thisInput = $(this).closest('.form-links').prev('.datepicker').find('.form-control');
        var tmrw = new Date((new Date()).valueOf() + 1000 * 3600 * 24).toISOString().substring(0, 10)
        thisInput.val(tmrw);
    });
}
/* Datatable custom pagination */
function calcDisableClasses(oSettings) {
    var start = oSettings._iDisplayStart;
    var length = oSettings._iDisplayLength;
    var visibleRecords = oSettings.fnRecordsDisplay();
    var all = length === -1;
    // Gordey Doronin: Re-used this code from main jQuery.dataTables source code. To be consistent.
    var page = all ? 0 : Math.ceil(start / length);
    var pages = all ? 1 : Math.ceil(visibleRecords / length);
    var disableFirstPrevClass = (page > 0 ? '' : oSettings.oClasses.sPageButtonDisabled);
    var disableNextLastClass = (page < pages - 1 ? '' : oSettings.oClasses.sPageButtonDisabled);
    return {
        'first': disableFirstPrevClass,
        'previous': disableFirstPrevClass,
        'next': disableNextLastClass,
        'last': disableNextLastClass
    };
}
function calcCurrentPage(oSettings) {
    return Math.ceil(oSettings._iDisplayStart / oSettings._iDisplayLength) + 1;
}
function calcPages(oSettings) {
    return Math.ceil(oSettings.fnRecordsDisplay() / oSettings._iDisplayLength);
}
var firstClassName = 'first';
var previousClassName = 'previous';
var nextClassName = 'next';
var lastClassName = 'last';
var paginateClassName = 'paginate';
var paginateOfClassName = 'paginate_of';
var paginatePageClassName = 'paginate_page';
var paginateInputClassName = 'paginate_input';
$.fn.dataTableExt.oPagination.input = {
    'fnInit': function (oSettings, nPaging, fnCallbackDraw) {
        var nFirst = document.createElement('span');
        var nPrevious = document.createElement('span');
        var nNext = document.createElement('span');
        var nLast = document.createElement('span');
        var nInput = document.createElement('input');
        var nPage = document.createElement('span');
        var nOf = document.createElement('span');
        var language = oSettings.oLanguage.oPaginate;
        var classes = oSettings.oClasses;
        nFirst.innerHTML = language.sFirst;
        nPrevious.innerHTML = language.sPrevious;
        nNext.innerHTML = language.sNext;
        nLast.innerHTML = language.sLast;
        nFirst.className = firstClassName + ' ' + classes.sPageButton;
        nPrevious.className = previousClassName + ' ' + classes.sPageButton;
        nNext.className = nextClassName + ' ' + classes.sPageButton;
        nLast.className = lastClassName + ' ' + classes.sPageButton;
        nOf.className = paginateOfClassName;
        nPage.className = paginatePageClassName;
        nInput.className = paginateInputClassName;
        if (oSettings.sTableId !== '') {
            nPaging.setAttribute('id', oSettings.sTableId + '_' + paginateClassName);
            nFirst.setAttribute('id', oSettings.sTableId + '_' + firstClassName);
            nPrevious.setAttribute('id', oSettings.sTableId + '_' + previousClassName);
            nNext.setAttribute('id', oSettings.sTableId + '_' + nextClassName);
            nLast.setAttribute('id', oSettings.sTableId + '_' + lastClassName);
        }
        nInput.type = 'text';
        nPage.innerHTML = 'Lehekülg: ';
        nPaging.appendChild(nPage);
        nPaging.appendChild(nInput);
        //nPaging.appendChild(nOf);
        nPaging.appendChild(nFirst);
        nPaging.appendChild(nPrevious);
        nPaging.appendChild(nNext);
        nPaging.appendChild(nLast);
        $(nFirst).click(function () {
            var iCurrentPage = calcCurrentPage(oSettings);
            if (iCurrentPage !== 1) {
                oSettings.oApi._fnPageChange(oSettings, 'first');
                fnCallbackDraw(oSettings);
            }
        });
        $(nPrevious).click(function () {
            var iCurrentPage = calcCurrentPage(oSettings);
            if (iCurrentPage !== 1) {
                oSettings.oApi._fnPageChange(oSettings, 'previous');
                fnCallbackDraw(oSettings);
            }
        });
        $(nNext).click(function () {
            var iCurrentPage = calcCurrentPage(oSettings);
            if (iCurrentPage !== calcPages(oSettings)) {
                oSettings.oApi._fnPageChange(oSettings, 'next');
                fnCallbackDraw(oSettings);
            }
        });
        $(nLast).click(function () {
            var iCurrentPage = calcCurrentPage(oSettings);
            if (iCurrentPage !== calcPages(oSettings)) {
                oSettings.oApi._fnPageChange(oSettings, 'last');
                fnCallbackDraw(oSettings);
            }
        });
        $(nInput).keyup(function (e) {
            // 38 = up arrow, 39 = right arrow
            if (e.which === 38 || e.which === 39) {
                this.value++;
            }
            // 37 = left arrow, 40 = down arrow
            else if ((e.which === 37 || e.which === 40) && this.value > 1) {
                this.value--;
            }
            if (this.value === '' || this.value.match(/[^0-9]/)) {
                /* Nothing entered or non-numeric character */
                this.value = this.value.replace(/[^\d]/g, ''); // don't even allow anything but digits
                return;
            }
            var iNewStart = oSettings._iDisplayLength * (this.value - 1);
            if (iNewStart < 0) {
                iNewStart = 0;
            }
            if (iNewStart >= oSettings.fnRecordsDisplay()) {
                iNewStart = (Math.ceil((oSettings.fnRecordsDisplay()) / oSettings._iDisplayLength) - 1) * oSettings._iDisplayLength;
            }
            oSettings._iDisplayStart = iNewStart;
            fnCallbackDraw(oSettings);
        });
        // Take the brutal approach to cancelling text selection.
        $('span', nPaging).bind('mousedown', function () {
            return false;
        });
        $('span', nPaging).bind('selectstart', function () {
            return false;
        });
        // If we can't page anyway, might as well not show it.
        var iPages = calcPages(oSettings);
        if (iPages <= 1) {
            $(nPaging).hide();
        }
    },
    'fnUpdate': function (oSettings) {
        if (!oSettings.aanFeatures.p) {
            return;
        }
        var iPages = calcPages(oSettings);
        var iCurrentPage = calcCurrentPage(oSettings);
        var an = oSettings.aanFeatures.p;
        if (iPages <= 1) // hide paging when we can't page
        {
            $(an).hide();
            return;
        }
        var disableClasses = calcDisableClasses(oSettings);
        $(an).show();
        // Enable/Disable `first` button.
        $(an).children('.' + firstClassName)
            .removeClass(oSettings.oClasses.sPageButtonDisabled)
            .addClass(disableClasses[firstClassName]);
        // Enable/Disable `prev` button.
        $(an).children('.' + previousClassName)
            .removeClass(oSettings.oClasses.sPageButtonDisabled)
            .addClass(disableClasses[previousClassName]);
        // Enable/Disable `next` button.
        $(an).children('.' + nextClassName)
            .removeClass(oSettings.oClasses.sPageButtonDisabled)
            .addClass(disableClasses[nextClassName]);
        // Enable/Disable `last` button.
        $(an).children('.' + lastClassName)
            .removeClass(oSettings.oClasses.sPageButtonDisabled)
            .addClass(disableClasses[lastClassName]);
        // Paginate of N pages text
        $(an).children('.' + paginateOfClassName).html(' of ' + iPages);
        // Current page numer input value
        $(an).children('.' + paginateInputClassName).val(iCurrentPage);
    }
};
/* tables */
$(window).on("load", function () {
    $(".table-wrap.scrollable").mCustomScrollbar({
        axis: "x",
        theme: "riha"
    });
});
$('body').on('click', '.table.dataTable tbody td.details-control', function () {
    hideEmptyLabels();
});
function hideEmptyLabels() {
    $('.table.dataTable tbody .dtr-details .dtr-title').each(function () {
        if ($(this).text() == '') {
            $(this).remove();
        }
    });
}
$('.table-01').DataTable({
    bInfo: false,
    searching: false,
    paging: false,
    bAutoWidth: false,
    columnDefs: [
        {
            targets: 'no-sort',
            orderable: false
        }
],
    "aaSorting": [[1, 'asc']]
});
$('.table-02').on('init.dt', function () {
    var dataTable = $(this).closest('.dataTables_wrapper');
    dataTable.addClass('table-pagination');
    dataTable.find('.dataTables_length select').wrap('<div class="input"></div>');
    dataTable.find('.dataTables_info').insertAfter(dataTable.find('.dataTables_paginate .paginate_input'))
}).DataTable({
    dom: '<"top">rt<"bottom"lip><"clear">',
    language: {
        "info": "_START_-_END_ _TOTAL_st",
        "lengthMenu": "Näita ridu _MENU_",
        "paginate": {
            "first": "Esimene",
            "last": "Viimane",
            "next": "Järgmine",
            "previous": "Eelmine"
        }
    },
    searching: false,
    bSort: false,
    bAutoWidth: false,
    pagingType: "input",
    columnDefs: [
        {
            targets: 'no-sort',
            orderable: false
        }
],
    "aaSorting": [[1, 'asc']]
});
$('.table-03').on('responsive-resize.dt', function () {
    hideEmptyLabels();
}).DataTable({
    bInfo: false,
    searching: false,
    paging: false,
    bAutoWidth: false,
    bSort: false
});
$('.table-04').DataTable({
    bInfo: false,
    searching: false,
    paging: false,
    bAutoWidth: false,
    bSort: false,
    language: {
        "emptyTable": "Andmed puuduvad"
    }
});
/* For proto only */
if (window.location.href.indexOf('_index') >= 1) {
    $('body').addClass('frontpage');
}
