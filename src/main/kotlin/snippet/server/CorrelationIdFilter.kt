package snippet.server

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.UUID

@Component
@Order(1)
class CorrelationIdFilter : OncePerRequestFilter() {
    companion object {
        const val CORRELATION_ID_KEY = "correlation-id"
        const val CORRELATION_ID_HEADER = "X-Correlation-Id"
    }

    public override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val correlationId: String = request.getHeader(CORRELATION_ID_HEADER) ?: UUID.randomUUID().toString()
        MDC.put(CORRELATION_ID_KEY, correlationId)
        try {
            filterChain.doFilter(request, response)
        } catch (e: Exception) {
            logger.error("Exception processing request", e)
            throw e
        } finally {
            MDC.remove(CORRELATION_ID_KEY)
            logger.info(request.method + " " + request.requestURI + " - " + response.status + "correlationId: " + correlationId)
        }
    }
}
