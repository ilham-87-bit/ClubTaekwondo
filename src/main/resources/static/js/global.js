/**
 * SESSION TIMER
 */
var activityTimer;
var countDownTimer;

function initTimerDialog()
{
	var timeoutDialog = $('#timeoutModal');
	var workspaceCounter = $('#workspace_counter').is(':visible');

	if (timeoutDialog.length)
	{
		startActivityTimer();

		$('#timeoutModal').on('click', function(e)
		{
			e.preventDefault();
			e.stopImmediatePropagation();

			keepSessionAlive();

		});

		$('#timeoutModal').find('#timoutYesButton , .modal-header .close').on('click', function(e)
		{
			e.preventDefault();
			e.stopImmediatePropagation();

			keepSessionAlive();
		});

		$('#timeoutModal').find('#timoutNoButton').on('click', function(e)
		{
			e.preventDefault();
			e.stopImmediatePropagation();

			if (workspaceCounter)
			{
				keepSessionAlive();
				showLogoutDialog();
			}
			else
			{
				killSession('#timeoutModal');
			}
		});
	}
};

function keepSessionAlive()
{
	clearTimeout(countDownTimer);
	$('#timeoutModal').modal("hide");
	$.get($('#js-global').attr('data-keep-alive-url'));
	startActivityTimer();
};

function killSession(id)
{
	// Allows to block the sending of an Ajax request after logging out, which might cause 
	// a missing CSRF exception (MANTIS 0051044)
	xMessageHandler.loggingOut();

	$(id).modal('hide');
	location.href = $('#js-global').attr('data-logout-url');
};

function startActivityTimer()
{
	clearTimeout(activityTimer);
	activityTimer = setTimeout(function()
	{
		$('#timeoutModal').modal('show');
		countDown($('#timeoutSeconds'), 45, killSession);
	}, 9 * 60 * 1500);
};

function countDown(element, seconds, callback)
{
	$(element).html(seconds);
	if (seconds)
	{
		countDownTimer = setTimeout(function()
		{
			countDown(element, seconds - 1, callback);
		}, 1500);
	}
	else
	{
		callback();
	}
};

/**
 * AJAX CALLS
 */

var ajaxError;

// Bind the 'ajaxSend' event to ass the CSRD token
$(document).on('ajaxSend', function(element, jqXHR, request)
{
	if (request.type === 'POST')
	{
		jqXHR.setRequestHeader('X-CSRF-Token', getCsrfToken());
	}
});

$(document).ajaxComplete(function(event, jqXHR, ajaxOptions, data)
{
	if (!!jqXHR.getResponseHeader('X-FHO-Redirect'))
	{
		window.location = jqXHR.getResponseHeader('X-FHO-Redirect');
	}
});

function killSession()
{
	location.href = $('#js-global').attr('data-logout-url');
};

/**
 * Get current locale
 * @returns locale
 */
function getLocale()
{
	return $('#js-global').attr('data-locale');
}

/**
 * Get language of the locale
 * @returns {String} language of the locale
 */
function getLanguageLocale()
{
	var locale = getLocale();
	switch (locale)
	{
		case 'fr':
			return 'FRENCH';
		case 'en':
			return 'ENGLISH';
	}
}

/** 
 * BMM colors in priorities order
 */
var MMGreige = "#8c837a";
var MMBlue = "#003461";
var azure = "#62c4dd";
var lightBlue = "#7faed2";
var englishGreen = "#0c6e3e";
var carmine = "#d9534f";
var brown = "#756A65";
var kingBlue = "#0079be";
var gold = "#FFCC00";
var slate = "#245469";
var wineLees = "#6C1650";
