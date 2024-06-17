/** 
 Отображение ошибок в div-контейнере (создается заранее)
 - containerId - идентификатор div-контейнера, в который будут выведены ошибки от сервера 
 - errorArray - массив строк с ошибками
*/
function viewErrors(containerId,errorArray) {
	var container = document.getElementById(containerId);
	if (!container)
		return;
	container.innerHTML = '';
	for (var i=0;i<errorArray.length;i++) {
		var div = document.createElement("DIV");
		div.className="alert alert-danger alert-form-content";
		div.innerHTML = errorArray[i];
		container.appendChild(div);
	}
	window.location.hash= '#'+containerId;
};

/**
Проверка данных формы на обязательность заполнения формы
- formId - ид формы, 
- alertId - div-контейнер, в который будут выведены ошибки от сервера 
- emptyText - текст ошибки при внутренней ошибке со стороны скрипта
*/
function checkFormOnRequired(formId,alertId,emptyText) {
	var form = document.forms[formId];
	var elems_req = form.querySelectorAll('input[aria-required="true"], select[aria-required="true"]')
	var error = false;
	if (elems_req.length>0) {
		AUI().use('aui-base',function(A) {
			for (var i=0;i<elems_req.length;i++) {
				if (A.one('#'+elems_req[i].id).get('value')=='') {
					error = true;
					break;
				}
			}
		});
	}
	if (error)
		viewErrors(alertId,[emptyText]);
	return !error;
};

/**
ajax-отправка данных формы с использованием Liferay.Util.fetch, тип запроса POST
- formId - форма, которую нужно отправить на сервер, 
- url - ссылка, 
- errorContainer - div-контейнер, в который будут выведены ошибки от сервера 
- errorText - текст ошибки при внутренней ошибке со стороны сервера
- callback - функция, которая будет вызвана после успешного выполнения
*/
function sendFormByAjax(formId,url,errorContainer,errorText,callback) {
	Liferay.Util.fetch(url, {
		body: new FormData(document.forms[formId]),
		method: 'POST'
	}).then(function(response) {
		return response.json();
	}).then(callback).catch(function(e) {
		viewErrors(errorContainer,[errorText]);
		console.log(e);
	});
};

/**
ajax-запрос данных с использованием Liferay.Util.fetch, тип запроса GET
	- url - ссылка, 
	- errorContainer - div-контейнер, в который будут выведены ошибки от сервера 
	- errorText - текст ошибки при внутренней ошибке со стороны сервера
	- callback - функция, которая будет вызвана после успешного выполнения
*/
function getContentByAjax(url,errorContainer,errorText,callback) {
	Liferay.Util.fetch(url, {
		method: 'GET'
	}).then(function(response) {
		return response.json();
	}).then(callback).catch(function(e) {
		viewErrors(errorContainer,[errorText]);
		console.log(e);
	});
};