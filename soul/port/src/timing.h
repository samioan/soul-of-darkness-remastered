#pragma once
#include <algorithm>
#include <cmath>
#include <stdexcept>

namespace soul {
// e.run(): sleep(max(1, 83 - elapsed)). Keep presentation independent of simulation.
constexpr double kLogicStep = 0.083;
constexpr int kHeight = 320;
struct Clock {
    double remainder = 0;
    unsigned long long ticks = 0;
    template<class Update> void advance(double dt, Update update) {
        if (!std::isfinite(dt) || dt < 0) throw std::runtime_error("Invalid clock delta");
        // Limit catch-up after moving the window, debugging or losing focus.
        remainder += std::min(dt, 0.25);
        while (remainder + 1e-10 >= kLogicStep) {
            update();
            ++ticks;
            remainder = std::max(0.0, remainder - kLogicStep);
        }
    }
    double alpha() const { return remainder / kLogicStep; }
};
}
