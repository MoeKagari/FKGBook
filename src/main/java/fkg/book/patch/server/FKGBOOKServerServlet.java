package fkg.book.patch.server;

import fkg.book.patch.api.GetMaster;
import fkg.book.patch.api.GetTurningCardSheet;
import server.CommunicationHandler;
import server.ProxyServerServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;

@SuppressWarnings("serial")
public class FKGBOOKServerServlet extends ProxyServerServlet {
	protected FKGBOOKServerServlet(IntSupplier listenPort, BooleanSupplier useProxy, IntSupplier proxyPort) {
		super(listenPort, useProxy, proxyPort);
	}

	/*
	//用来判断是否跳过某类剧情
	private static class SkipEventPredicate {
		public final static byte[] EMPTY_TEXT_RESPONSE = new byte[0];
		public final static List<SkipEventPredicate> skipEventPredicateList = Arrays.asList(
				new SkipEventPredicate(FKGBOOKApplicationMain.getMainConfig().getSkipEventConfig()::isSkipEventHscene, (serverName, uri) -> uri.startsWith("/product/event/hscene/")),
				new SkipEventPredicate(FKGBOOKApplicationMain.getMainConfig().getSkipEventConfig()::isSkipEventHsceneR18, (serverName, uri) -> uri.startsWith("/product/event/hscene_r18/")),
				new SkipEventPredicate(FKGBOOKApplicationMain.getMainConfig().getSkipEventConfig()::isSkipEventNew, (serverName, uri) -> uri.startsWith("/product/event/new/")),
				new SkipEventPredicate(FKGBOOKApplicationMain.getMainConfig().getSkipEventConfig()::isSkipEventPerformance, (serverName, uri) -> uri.startsWith("/product/event/performance/")),
				new SkipEventPredicate(FKGBOOKApplicationMain.getMainConfig().getSkipEventConfig()::isSkipEventStory, (serverName, uri) -> uri.startsWith("/product/event/story/")),
				new SkipEventPredicate(FKGBOOKApplicationMain.getMainConfig().getSkipEventConfig()::isSkipEventTutorial, (serverName, uri) -> uri.startsWith("/product/event/tutorial/"))
		);
		public final BooleanSupplier enable;
		public final BiPredicate<String, String> predicate;

		public SkipEventPredicate(BooleanSupplier enable, BiPredicate<String, String> predicate) {
			this.enable = enable;
			this.predicate = predicate;
		}

		public boolean test(String serverName, String uri) {
			return this.enable.getAsBoolean() && this.predicate.test(serverName, uri);
		}
	}
	*/

	@Override protected void service(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {
		/*
		//跳过剧情
		if(FKGBOOKApplicationMain.getMainConfig().isSkipEvent()) {
			String serverName = httpRequest.getServerName();
			String uri = httpRequest.getRequestURI();
			if(SkipEventPredicate.skipEventPredicateList.stream().anyMatch(sep -> sep.test(serverName, uri))) {
				httpResponse.getOutputStream().write(SkipEventPredicate.EMPTY_TEXT_RESPONSE);
				return;
			}
		}
		*/

		super.service(httpRequest, httpResponse);
	}

	@Override
	protected CommunicationHandler getHandler(String serverName, String uri) {
		if(FKGBOOKApiHandler.SERVERNAME.equals(serverName)) {
			switch(uri) {
		/*
				case GetBook.URI:
					if(FKGBOOKApplicationMain.getMainConfig().isUseBookPatch()) {
						return new GetBook();
					}
					break;
				case Login.URI:
					if(FKGBOOKApplicationMain.getMainConfig().isReplaceDeputyLeader()) {
						return new Login(FKGBOOKApplicationMain.getMainConfig().getTargetDeputyLeaderId());
					}
					break;
		*/
				case GetMaster.URI:
					return new GetMaster();
				case GetTurningCardSheet.URI:
					return new GetTurningCardSheet();
			}
		}

		return super.getHandler(serverName, uri);
	}
}
