import jakarta.servlet.FilterChain
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.slf4j.MDC
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import snippet.server.CorrelationIdFilter
import snippet.server.CorrelationIdFilter.Companion.CORRELATION_ID_KEY
import snippet.server.RequestLogFilter
import java.util.UUID

class ServerTests {
    @Test
    fun `test CorrelationIdFilter with existing header`() {
        val filter = CorrelationIdFilter()
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        val filterChain = mock(FilterChain::class.java)
        val correlationId = UUID.randomUUID().toString()
        request.addHeader(CorrelationIdFilter.CORRELATION_ID_HEADER, correlationId)

        filter.doFilterInternal(request, response, filterChain)

        verify(filterChain).doFilter(request, response)
        MDC.remove(CorrelationIdFilter.CORRELATION_ID_KEY)
    }

    @Test
    fun `test CorrelationIdFilter without existing header`() {
        val filter = CorrelationIdFilter()
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        val filterChain = mock(FilterChain::class.java)

        filter.doFilterInternal(request, response, filterChain)

        verify(filterChain).doFilter(request, response)
        MDC.remove(CORRELATION_ID_KEY)
    }

    @Test
    fun `test RequestLogFilter`() {
        val filter = RequestLogFilter()
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        val filterChain = mock(FilterChain::class.java)
        request.requestURI = "/test"
        request.method = "GET"

        filter.doFilterInternal(request, response, filterChain)

        verify(filterChain).doFilter(request, response)
    }
}
