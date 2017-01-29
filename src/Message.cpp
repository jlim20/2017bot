#include "Message.h"

#include <array>
#include <ctime>
#include <sstream>

using std::array;
using std::string;
using std::stringstream;

using std::tm;
using std::time;
using std::time_t;
using std::strftime;

namespace robot {

// =========================================================================
// Get the current timestamp, or an error message string if that fails.

string Message::timestamp() const {
    time_t now = time(nullptr);
    tm localTime;
    localtime_r(&now, &localTime);

    array<char, 100> buffer;
    if (strftime(&buffer[0], buffer.size(), "%c %Z", &localTime)) {
        stringstream stream;
        stream << &buffer[0];
        return stream.str();
    }

    return "<Error: Buffer size too small to hold timestamp>";
}

// =========================================================================
// The Message base class is responsible for constructing the overall XML
// message from its various pieces (including the message payload, which is
// usually also XML.)
//
// We don't use a real XML parser for this -- there's simply no need on the
// transmission side.

Message::operator string() const {
    string payload = str();
    stringstream stream;

    stream << "<message><type>" << name() << "</type><timestamp>" << timestamp() << "</timestamp><data>";
    if (!payload.empty()) {
        stream << payload;
    }
    stream << "</data></message>";

    // The Java parser on the server-side uses BufferedReader.readLine() to
    // gather the string it expects to be a small XML document, so for its
    // benefit, all messages will be complete lines unto themselves.
    stream << "\n";

    return stream.str();
}


// =========================================================================
// Heartbeat messages are empty -- only the timestamp matters.

HeartbeatMessage::HeartbeatMessage() { }
string HeartbeatMessage::str() const { return ""; }
string HeartbeatMessage::name() const { return "heartbeat"; }

// =========================================================================
// Log messages -- take heartbeat, add string.

LogMessage::LogMessage(const string& s) : message(s) { }
string LogMessage::str() const { return message; }
string LogMessage::name() const { return "log"; }


} // end (namespace robot)
