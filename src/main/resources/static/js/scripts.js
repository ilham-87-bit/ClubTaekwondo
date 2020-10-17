// ---
// Scripts module loader
// ---

// Define all modules
// var scripts = {
// 	init : function(selector)
// 	{
// 		scripts.blockLinks.init(selector); // export a link to a full block
// 		scripts.inputField.init(selector); //
// 		scripts.checkAll.init(selector); // check/uncheck all checkboxes
// 		scripts.disableSelect.init(selector);
// 	},
// 	onload : function()
// 	{
// 		$(docReady.init);
//
// 	}
// };
//
// //Initialize the modules
// $(scripts.init);
// $(scripts.onload);
//
// // ---
// // Block links
// // -
// // Transfer a link to a block
// // ---
// scripts.blockLinks = {
// 	init : function(selector)
// 	{
// 		selector = initSelector(selector);
// 		var rowLinkAlt = $(selector + '.js-row-link-alt');
// 		var rowLinkTarget = $(selector + '.js-row-target');
//
// 		// Put download links on row
// 		rowLinkAlt.find('td:not(.js-no-link)').on('click', function(e)
// 		{
// 			var link = $(this).closest('.js-row-link-alt').find(rowLinkTarget).attr('href');
//
// 			// Go to link
// 			window.location = link;
// 		});
//
// 	}
// };
//
// //---
// //
// //-
// //
// //---
// scripts.inputField = {
// 	init : function(selector)
// 	{
//
// 		selector = initSelector(selector);
// 		var inputNumber = $(selector + '.input-number');
//
// 		inputNumber.bind("cut copy paste", function(e)
// 		{
// 			e.preventDefault();
// 		});
//
// 		// Put download links on row
// 		inputNumber.on('keypress', function(event)
// 		{
// 			if (event.charCode === 0 || event.charCode >= 48 && event.charCode <= 57 && !event.ctrlKey)
// 			{
// 				return true;
// 			}
// 			return false;
// 		});
//
// 		inputNumber.on('change', function(e)
// 		{
// 			setInputNumberBetweenMinMax($(this));
// 			return true;
// 		});
//
// 	}
// };
//
// //Check/uncheck all checkboxes
// //---
// scripts.checkAll = {
// 	init : function(selector)
// 	{
// 		// Use the 'is-hidden' class to hide your elements"
// 		selector = initSelector(selector);
// 		var toggleAll = $(selector + '.js-select-all');
//
// 		// Toggle functionality
// 		toggleAll.on('click', function(e)
// 		{
// 			$(this).toggleClass('is-active');
// 			$(this).addClass('toggling');
//
// 			$(this).parents('table').find('input[type=checkbox]:not(.is-active)').each(function()
// 			{
// 				if ($(this).parents('table').find('.js-select-all').hasClass('is-active') === true)
// 				{
// 					if ($(this).prop('checked') === false)
// 					{
// 						$(this).prop('checked', true);
// 					}
// 				}
// 				else
// 				{
// 					if ($(this).prop('checked') === true)
// 					{
// 						$(this).prop('checked', false);
// 					}
// 				}
// 			});
//
// 			$(this).removeClass('toggling');
// 		});
// 	}
// };
//
// //---
// //Select
// //-
// //Add class disabled on select awesome wrapper
// //---
// scripts.disableSelect = {
// 	init : function(selector)
// 	{
// 		selector = initSelector(selector);
// 		var select = $(selector + 'select');
//
// 		// Change Class wrapper on load
// 		disableSelect(select);
//
// 		// Change Class wrapper on change
// 		select.on('disabled', function()
// 		{
// 			disableSelect($(this));
// 		});
//
// 		function disableSelect(element)
// 		{
// 			for (var i = 0; i < element.length; i++)
// 			{
// 				var parent = $(element[i]).parents('.select-awesome');
// 				if ($(element[i]).prop("disabled") === true)
// 				{
// 					parent.addClass("disabled");
// 				}
// 				else
// 				{
// 					parent.removeClass("disabled");
// 				}
// 			}
// 		}
//
// 	}
// };
