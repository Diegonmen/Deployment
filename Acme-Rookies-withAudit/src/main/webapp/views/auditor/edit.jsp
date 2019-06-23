<%@page import="org.springframework.context.i18n.LocaleContextHolder"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<jstl:set value="<%=LocaleContextHolder.getLocale()%>" var="locale"></jstl:set>


<head>
<style>
.modal {
	display: none;
	position: fixed;
	z-index: 1;
	padding-top: 100px;
	left: 0;
	top: 0;
	width: 100%;
	height: 100%;
	background-color: rgb(0, 0, 0);
	background-color: rgba(0, 0, 0, 0.4);
	overflow: auto;
}

.modal-content {
	background-color: #fefefe;
	margin: auto;
	padding: 20px;
	border: 1px solid #888;
	width: 65%;
}

.close {
	color: #aaaaaa;
	float: right;
	font-size: 28px;
	font-weight: bold;
}

.close:hover, .close:focus {
	color: #000;
	text-decoration: none;
	cursor: pointer;
}
</style>
</head>


<form:form action="auditor/edit.do"
	modelAttribute="auditorForm" id="form">

	<acme:hiddenInputs attributes="id" />

	<acme:textbox code="auditor.userAccount.username" path="username" />

	
	<acme:password code="auditor.userAccount.newPassword"
		path="newPassword" />
	<acme:password code="auditor.userAccount.confirmPassword"
		path="confirmPassword" />
	<acme:textbox code="auditor.name" path="name" />
	<acme:textbox code="auditor.surname" path="surname" />
	<acme:textbox code="auditor.email" path="email" />
	<acme:textbox code="auditor.picture" path="picture" />
	<acme:textbox code="auditor.phoneNumber" path="phoneNumber" />
	<acme:textbox code="auditor.address" path="address" />
	<acme:textbox code="auditor.vat" path="vat" vatPlaceholder="true"/>
	
	
	<acme:textbox code="auditor.creditCard.number" path="number" />
	<acme:textbox code="auditor.creditCard.cvv" path="cvv" typeNumber="true" />
	<acme:textbox code="auditor.creditCard.holderName" path="holderName" />
	<acme:textbox code="auditor.creditCard.make" path="make" />
	<acme:textbox code="auditor.creditCard.expirationMonth" path="expirationMonth" typeNumber="true"/>
	<acme:textbox code="auditor.creditCard.expirationYear" path="expirationYear" typeNumber="true"/>

	<jstl:if test="${auditorForm.id == 0}">
		<input type="checkbox" name="terms">
		<spring:message code="terms.accept"></spring:message>
		<br>
	</jstl:if>
	
	<jstl:if test="${auditorForm.id > 0}">
		<input type="submit" name="save"
			value="<spring:message code="auditor.save" />"
			onclick="if(!/^(\+[0-9]{1,3}[ ]{0,1}(\([0-9]{1,3}\)[ ]{0,1}){0,1}){0,1}[0-9]{1}[0-9 ]{3,}$/.test(document.getElementById('phoneNumber').value)) { return confirm('<spring:message code="administrator.confirm.phoneNumber" />')}" />
	</jstl:if>

	<jstl:if test="${auditorForm.id == 0}">
		<acme:editButtons entity="auditor" role="auditor"
			save="true" customCancelPath="welcome/index.do"
			checkPhoneNumber="true" />

	</jstl:if>

</form:form>

<jstl:if test="${auditorForm.id == 0}">

	<button id="myBtn">Terms and Conditions</button>

	<div id="myModal" class="modal">

		<div class="modal-content">
			<span class="close">&times;</span>
			<jstl:if test="${locale == 'en' }">
				<h2>Welcome to Acme-Rookie</h2>
				<p>These terms and conditions outline the rules and regulations
					for the use of Acme-Rookie's Website, service provided by
					Acme-Rookie Inc.</p>
				<br />
				<span style="text-transform: capitalize;"> Acme-Rookie Inc.</span> is located at:<br />
				<address>
					Av. Reina Mercedes s/n , Sevilla<br />Andalucia - 41012, Spain<br />
				</address>
				<p>By accessing this website we assume you accept these terms
					and conditions in full. Do not continue to use Acme-Rookie's
					website if you do not accept all of the terms and conditions stated
					on this page.</p>
				<p>The following terminology applies to these Terms and
					Conditions, Privacy Statement and Disclaimer Notice and any or all
					Agreements: 'Client', 'You' and 'Your' refers to you, the person
					accessing this website and accepting the Company's terms and
					conditions. 'The Company', 'Ourselves', 'We', 'Our' and 'Us',
					refers to our Company. 'Party', 'Parties', or 'Us', refers to both
					the Client and ourselves, or either the Client or ourselves. All
					terms refer to the offer, acceptance and consideration of payment
					necessary to undertake the process of our assistance to the Client
					in the most appropriate manner, whether by formal meetings of a
					fixed duration, or any other means, for the express purpose of
					meeting the Client's needs in respect of provision of the Company's
					stated services/products, in accordance with and subject to,
					prevailing law of Spain. Any use of the above terminology or other
					words in the singular, plural, capitalisation and/or he/she or
					they, are taken as interchangeable and therefore as referring to
					same.</p>
				<h2>Cookies</h2>
				<p>We employ the use of cookies. By using Acme-Rookie's website
					you consent to the use of cookies in accordance with Acme-Rookie's
					Inc. privacy policy.</p>
				<p>Most of the modern day interactive web sites use cookies to
					enable us to retrieve user details for each visit. Cookies are used
					in some areas of our site to enable the functionality of this area
					and ease of use for those people visiting. Some of our affiliate /
					advertising partners may also use cookies.</p>
				<h2>License</h2>
				<p>Unless otherwise stated, Acme-Rookie Inc. and/or it's
					licensors own the intellectual property rights for all material on
					Acme-Rookie. All intellectual property rights are reserved. You may
					view and/or print pages from https://acme.com for your own personal
					use subject to restrictions set in these terms and conditions.</p>
				<p>You must not:</p>
				<ol>
					<li>Republish material from https://acme.com</li>
					<li>Sell, rent or sub-license material from https://acme.com</li>
					<li>Reproduce, duplicate or copy material from
						https://acme.com</li>
				</ol>
				<p>Redistribute content from Acme-Rookie (unless content is
					specifically made for redistribution).</p>
				<h2>User Comments</h2>
				<ol>
					<li>This Agreement shall begin on the date hereof.</li>
					<li>Certain parts of this website offer the opportunity for
						users to post and exchange opinions, information, material and
						data ('Comments') in areas of the website. Acme-Rookie does not
						screen, edit, publish or review Comments prior to their appearance
						on the website and Comments do not reflect the views or opinions
						ofAcme-Rookie, its agents or affiliates. Comments reflect the
						view and opinion of the person who posts such view or opinion. To
						the extent permitted by applicable laws Acme-Rookie Inc. shall
						not be responsible or liable for the Comments or for any loss
						cost, liability, damages or expenses caused and or suffered as a
						result of any use of and/or posting of and/or appearance of the
						Comments on this website.</li>
					<li>Acme-Rookie Inc. reserves the right to monitor all
						Comments and to remove any Comments which it considers in its
						absolute discretion to be inappropriate, offensive or otherwise in
						breach of these Terms and Conditions.</li>
					<li>You warrant and represent that:
						<ol>
							<li>You are entitled to post the Comments on our website and
								have all necessary licenses and consents to do so;</li>
							<li>The Comments do not infringe any intellectual property
								right, including without limitation copyright, patent or
								trademark, or other proprietary right of any third party;</li>
							<li>The Comments do not contain any defamatory, libelous,
								offensive, indecent or otherwise unlawful material or material
								which is an invasion of privacy</li>
							<li>The Comments will not be used to solicit or promote
								business or custom or present commercial activities or unlawful
								activity.</li>
						</ol>
					</li>
					<li>You hereby grant to <strong>Acme-Rookie Inc.</strong> a
						non-exclusive royalty-free license to use, reproduce, edit and
						authorize others to use, reproduce and edit any of your Comments
						in any and all forms, formats or media.
					</li>
				</ol>
				<h2>Iframes</h2>
				<p>Without prior approval and express written permission, you
					may not create frames around our Web pages or use other techniques
					that alter in any way the visual presentation or appearance of our
					Web site.</p>
				<h2>Reservation of Rights</h2>
				<p>We reserve the right at any time and in its sole discretion
					to request that you remove all links or any particular link to our
					Web site. You agree to immediately remove all links to our Web site
					upon such request. We also reserve the right to amend these terms
					and conditions and its linking policy at any time. By continuing to
					link to our Web site, you agree to be bound to and abide by these
					linking terms and conditions.</p>
				<h2>Removal of links from our website</h2>
				<p>If you find any link on our Web site or any linked web site
					objectionable for any reason, you may contact us about this. We
					will consider requests to remove links but will have no obligation
					to do so or to respond directly to you.</p>
				<p>Whilst we endeavour to ensure that the information on this
					website is correct, we do not warrant its completeness or accuracy;
					nor do we commit to ensuring that the website remains available or
					that the material on the website is kept up to date.</p>
				<h2>Content Liability</h2>
				<p>We shall have no responsibility or liability for any content
					appearing on your Web site. You agree to indemnify and defend us
					against all claims arising out of or based upon your Website. No
					link(s) may appear on any page on your Web site or within any
					context containing content or materials that may be interpreted as
					libelous, obscene or criminal, or which infringes, otherwise
					violates, or advocates the infringement or other violation of, any
					third party rights.</p>
				<h2>Disclaimer</h2>
				<p>To the maximum extent permitted by applicable law, we exclude
					all representations, warranties and conditions relating to our
					website and the use of this website (including, without limitation,
					any warranties implied by law in respect of satisfactory quality,
					fitness for purpose and/or the use of reasonable care and skill).
					Nothing in this disclaimer will:</p>
				<ol>
					<li>limit or exclude our or your liability for death or
						personal injury resulting from negligence;</li>
					<li>limit or exclude our or your liability for fraud or
						fraudulent misrepresentation;</li>
					<li>limit any of our or your liabilities in any way that is
						not permitted under applicable law; or</li>
					<li>exclude any of our or your liabilities that may not be
						excluded under applicable law.</li>
				</ol>
				<p>The limitations and exclusions of liability set out in this
					Section and elsewhere in this disclaimer: (a) are subject to the
					preceding paragraph; and (b) govern all liabilities arising under
					the disclaimer or in relation to the subject matter of this
					disclaimer, including liabilities arising in contract, in tort
					(including negligence) and for breach of statutory duty.</p>
				<p>To the extent that the website and the information and
					services on the website are provided free of charge, we will not be
					liable for any loss or damage of any nature.</p>
				<h2>Commitment of data protection and compliance with LSSICE</h2>
				<p>In accordance with Organic Law 15/1999 on Personal Data
					Protection, and Royal Decree 1720/2007 implementation of the
					Personal Data Protection Organic Law, we inform you that the use of
					certain services on our website requires that you provide certain
					personal data through registration forms or by sending email
					messages, and will be processed and incorporated into files owned
					and controlled by Acme-Rookie Inc.</p>
				<p>The voluntary submission of personal information, and the
					checked boxes in our forms, constitutes explicit consent to the
					treatment of your personal data according to the purposes mentioned
					in the forms.</p>
				<p>Please note that the acceptance of our privacy policy,
					provided you do not tell us otherwise, implies your consent to
					process your personal data to provide our services and, if you
					agree, receiving all kinds of information by any means including
					electronic media which Acme-Rookie Inc. or related entities send
					to report on their activities/news, courses, programs and any offer
					of services and products related to Acme-Rookie Inc. activities.</p>
				<p>Acme-Rookie is also committed to not collecting unnecessary
					information about the User and handling personal information which
					the User may provide through our website with extreme care.</p>
				<p>The user is solely responsible regarding the accuracy and
					updating of the data provided, through various forms of the
					website.</p>

				<p>Acme-Rookie informs you that your data is treated
					confidentially and used internally and for the purposes indicated.
					Therefore, we do not give them or communicate them to third
					parties, except as provided for by law, and to entities and
					organizations directly related to the services or products
					purchased. Any other communication would be previously requested.
					Even so, if you your personal data removed from the system, you may
					send an email to acme@gmail.com asking for it.</p>
				<h2></h2>
				<h2></h2>
			</jstl:if>
			<jstl:if test="${locale == 'es' }">
				<h2>Bienvenido a Acme-Rookie</h2>
				<p>Estos términos y condiciones describen las reglas y
					regulaciones para el uso del sitio web de Acme-Rookie, servicio
					provisto por Acme-Madrugá Inc.</p>
				<br />
				<span style="text-transform: capitalize;"> Acme-Madrugá Inc.</span> se
	encuentra en:
	<br />
				<address>
					Av. Reina Mercedes s/n, Sevilla<br />Andalucía - 41012, España<br />
				</address>
				<p>Al acceder a este sitio web, asumimos que usted acepta estos
					términos y condiciones en su totalidad. No seguir utilizando el
					sitio web de Acme-Rookie si no acepta todos los términos y
					condiciones establecidos en esta página.</p>
				<p>La siguiente terminología se aplica a estos Términos y
					Condiciones, Declaración de Privacidad y Aviso de Exención de
					Responsabilidad y cualquiera o todos los acuerdos: "Cliente",
					"Usted" y "Su" se refiere a usted, la persona que accede a este
					sitio web. y aceptando los términos y condiciones de la Compañía.
					"La Compañía", "Nosotros mismos", "Nosotros", "Nuestro" y
					"Nosotros", se refiere a nuestra compañía. "Parte", "Partes", o
					"Nosotros", se refiere tanto al Cliente como a nosotros mismos, o
					bien al Cliente. o a nosotros mismos. Todos los términos se
					refieren a la oferta, aceptación y consideración del pago necesario
					para llevar a cabo el proceso de nuestra asistencia al Cliente de
					la manera más adecuada, ya sea mediante reuniones formales de
					duración determinada, o por cualquier otro medio, con el fin
					expreso de satisfacer las necesidades del Cliente en materia de de
					la prestación de los servicios/productos declarados de la Compañía,
					de conformidad con la legislación vigente y con sujeción a ella de
					España. Cualquier uso de la terminología anterior u otras palabras
					en singular, plural, capitalización y/o se consideran
					intercambiables y, por lo tanto, se refieren a las mismas.</p>
				<h2>Cookies</h2>


				<p>Empleamos el uso de cookies. Al utilizar el sitio web de
					Acme-Rookie usted consiente el uso de cookies de acuerdo con la
					política de privacidad de Acme-Madrugá Inc.</p>
				<p>La mayoría de los sitios web interactivos de hoy en día
					utilizan cookies para poder recuperar los datos de los usuarios en
					cada visita. Las cookies se utilizan en algunas áreas de nuestro
					sitio web para permitir la funcionalidad de esta área y la
					facilidad de uso para las personas que la visitan. Algunos de
					nuestros los socios afiliados/publicitarios también pueden utilizar
					cookies.</p>
				<h2>Licencia</h2>
				<p>A menos que se indique lo contrario, Acme-Madrugá Inc. y/o
					sus licenciantes son titulares de los derechos de propiedad
					intelectual de todo el material sobre Acme-Rookie. Todos los
					derechos de propiedad intelectual están reservados. Usted puede ver
					y/o imprimir páginas de https://acme.com para su uso personal
					sujeto a las restricciones establecidas en estos términos y
					condiciones
				<p>No debe:</p>
				<ol>
					<li>Republicar material de https://acme.com</li>
					<li>Vender, alquilar o sublicenciar material de
						https://acme.com</li>
				</ol>
				<p>Redistribuir el contenido de Acme-Rookie (a menos que el
					contenido esté hecho específicamente para la redistribución).</p>
				<h2>Comentarios de los usuarios</h2>
				<ol>
					<li>El presente Acuerdo comenzará en la fecha del presente
						documento.</li>
					<li>Algunas partes de este sitio web ofrecen a los usuarios la
						oportunidad de publicar e intercambiar opiniones e información,
						material y datos ("Comentarios") en áreas del sitio web.
						Acme-Madrugá no filtra, edita, publica o revisar los Comentarios
						antes de su aparición en el sitio web y los Comentarios no
						reflejan los puntos de vista o opiniones de Acme-Madrugá Inc., sus
						agentes o afiliados. Los comentarios reflejan el punto de vista y
						la opinión del persona que publica dicha vista u opinión. En la
						medida en que lo permita la legislación aplicable, Acme-Madrugá no
						será responsable de los Comentarios o de cualquier pérdida, coste,
						responsabilidad, daños o gastos ocasionados y o sufrido como
						resultado de cualquier uso de y/o publicación y/o aparición de los
						Comentarios en este sitio web.</li>
					<li>Acme-Madrugá Inc. se reserva el derecho de controlar todos
						los Comentarios y de eliminar cualquier Comentario que considere
						oportuno. en su absoluta discreción de ser inapropiado, ofensivo o
						de otra manera en violación de estos Términos y Condiciones.</li>
					<li>Usted garantiza y declara que:
						<ol>
							<li>Usted tiene derecho a publicar los Comentarios en
								nuestro sitio web y tiene todas las licencias y consentimientos
								necesarios para hacerlo;</li>
							<li>Los Comentarios no infringen ningún derecho de propiedad
								intelectual, incluyendo sin limitación los derechos de autor,
								patente o marca comercial, u otro derecho de propiedad de
								terceros</li>
							<li>Los Comentarios no contienen ningún material
								difamatorio, calumnioso, ofensivo, indecente o de otro modo
								ilegal. o material que es una invasión de la privacidad</li>
							<li>Los Comentarios no serán utilizados para solicitar o
								promover negocios o costumbres o para presentar actividades
								comerciales. o actividades ilegales.</li>
						</ol>
					</li>
					<li>Por la presente, usted concede a <strong>Acme-Madrugá
							Inc.</strong> una licencia no exclusiva libre de regalías para su uso,
						reproducción, editar y autorizar a otros a usar, reproducir y
						editar cualquiera de sus Comentarios en todas y cada una de sus
						formas y formatos o medio.
					</li>
				</ol>
				<h2>Imágenes</h2>
				<p>Sin la aprobación previa y el permiso expreso por escrito,
					usted no puede crear marcos alrededor de nuestras páginas Web o
					utilizar otras técnicas que alteren de alguna manera la
					presentación visual o apariencia de nuestro sitio Web.</p>
				<h2>Reserva de derechos</h2>
				<p>Nos reservamos el derecho, en cualquier momento y a su entera
					discreción, de solicitarle que elimine todos los enlaces o
					cualquier otra información en particular. enlace a nuestro sitio
					Web. Usted acepta eliminar inmediatamente todos los enlaces a
					nuestro sitio web si así lo solicita. Nosotros también nos
					reservamos el derecho de modificar estos términos y condiciones y
					su política de enlaces en cualquier momento. Continuando para
					enlazar a nuestro sitio Web, usted acepta estar obligado y cumplir
					con estos términos y condiciones de enlace.</p>
				<h2>Eliminación de enlaces de nuestro sitio web</h2>
				<p>Si encuentra algún enlace en nuestro sitio web o algún sitio
					web enlazado objetable por cualquier razón, puede ponerse en
					contacto con sobre esto. Consideraremos las solicitudes de
					eliminación de enlaces, pero no tendremos la obligación de hacerlo
					ni de responder. directamente a usted.</p>
				<p>Aunque nos esforzamos por asegurarnos de que la información
					contenida en este sitio web es correcta, no garantizamos que sea
					completa. o exactitud; ni nos comprometemos a asegurar que el sitio
					web permanezca disponible o que el material en el El sitio web se
					mantiene actualizado.</p>
				<h2>Responsabilidad del contenido</h2>
				<p>No nos hacemos responsables de ningún contenido que aparezca
					en su sitio web. Usted acepta indemnizar y defendernos contra todas
					las reclamaciones que surjan de su sitio web o que se basen en él.
					Ningún enlace(s) puede(n) aparecer en cualquier en su sitio Web o
					dentro de cualquier contexto que contenga contenido o materiales
					que puedan ser interpretados como calumnioso, obsceno o criminal, o
					que infringe, de otra manera viola, o aboga por la infracción o
					otra violación de los derechos de terceros.</p>
				<h2>Descargo de responsabilidad</h2>
				<p>En la máxima medida permitida por la ley aplicable, excluimos
					todas las representaciones, garantías y condiciones relacionadas
					con nuestro sitio web y el uso de este sitio web (incluyendo, sin
					limitación, cualquier garantía implícita por ley con respecto a la
					calidad satisfactoria, la idoneidad para el propósito y/o el uso de
					cuidado razonable y habilidad). Nada en esta cláusula de exención
					de responsabilidad podrá:</p>
				<ol>
					<li>Limitar o excluir nuestra o su responsabilidad por muerte
						o lesiones personales resultantes de negligencia;</li>
					<li>Limitar o excluir nuestra o su responsabilidad por fraude
						o tergiversación fraudulenta;</li>
					<li>Limitar cualquiera de nuestras responsabilidades o las
						suyas de cualquier manera que no esté permitida por la ley
						aplicable;</li>
				</ol>
				<p>Las limitaciones y exclusiones de responsabilidad
					establecidas en esta Sección y en otras partes de esta exención de
					responsabilidad: (a) están sujetos al párrafo anterior; y (b)
					regirán todas las responsabilidades que surjan de la renuncia de
					responsabilidad o en relación con el objeto de la presente cláusula
					de exención de responsabilidad, incluidas las responsabilidades que
					se deriven de un contrato, en caso de responsabilidad
					extracontractual (incluida la negligencia) y por incumplimiento de
					las obligaciones legales.</p>
				<p>En la medida en que el sitio web y la información y servicios
					del mismo sean gratuitos, no seremos responsables de ninguna
					pérdida o daño de ninguna naturaleza.</p>
				<h2>Compromiso de protección de datos y cumplimiento de LSSICE</h2>
				<p>De acuerdo con la Ley Orgánica 15/1999 de Protección de Datos
					de Carácter Personal, y el Real Decreto 1720/2007 por el que se
					desarrolla la Ley Orgánica de Protección de Datos de Carácter
					Personal, le informamos que la utilización de determinados
					servicios de nuestra página web requiere que usted facilite
					determinados datos de carácter personal a través de formularios de
					registro o mediante el envío de mensajes de correo electrónico, y
					que sean tratados e incorporados a ficheros titularidad y
					controlados por Acme-Rookie Inc.</p>
				<p>El envío voluntario de datos personales, así como las
					casillas marcadas en nuestros formularios, constituye un
					consentimiento expreso para el tratamiento de sus datos personales
					de acuerdo con las finalidades indicadas en los mismos.</p>
				<p>Tenga en cuenta que la aceptación de nuestra política de
					privacidad, siempre que no nos indique lo contrario, implica su
					consentimiento para procesar sus datos personales con el fin de
					prestar nuestros servicios y, si está de acuerdo, recibir todo tipo
					de información por cualquier medio, incluidos los medios
					electrónicos que Acme-Rookie Inc. o entidades relacionadas envíen
					para informar sobre sus actividades/noticias, cursos, programas y
					cualquier oferta de servicios y productos relacionados con las
					actividades de Acme-Rookie Inc.</p>
				<p>Acme-Rookie Inc. también se compromete a no recopilar
					información innecesaria sobre el Usuario y a manejar la información
					personal que el Usuario pueda proporcionar a través de nuestro
					sitio web con extremo cuidado.</p>
				<p>El usuario es el único responsable de la veracidad y
					actualización de los datos facilitados, a través de los distintos
					formularios del sitio web.</p>
				<p>Acme-Rookie le informa que sus datos son tratados de forma
					confidencial y utilizados internamente y para las finalidades
					indicadas. Por lo tanto, no los entregamos ni los comunicamos a
					terceros, salvo en los casos previstos por la ley, ni a entidades y
					organizaciones directamente relacionadas con los servicios o
					productos adquiridos. Cualquier otra comunicación será solicitada
					previamente. Aún así, si desea que sus datos personales sean
					eliminados del sistema, puede enviar un correo electrónico a
					acme@gmail.com solicitándolo.</p>
				<h2></h2>
			</jstl:if>
		</div>
	</div>
</jstl:if>

<script>
	var modal = document.getElementById('myModal');

	var btn = document.getElementById("myBtn");

	var span = document.getElementsByClassName("close")[0];

	btn.onclick = function() {
		modal.style.display = "block";
	}

	span.onclick = function() {
		modal.style.display = "none";
	}

	window.onclick = function(event) {
		if (event.target == modal) {
			modal.style.display = "none";
		}
	}

	document.getElementById('form').onsubmit = function() {
		var terms = this.elements['terms'];
		if (!terms.checked) {
			alert('Please accept the terms and conditions.');
			return false;
		}
		return true;
	}
</script>